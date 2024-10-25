package codefun.netty_only.handler;

import codefun.netty_only.config.AppSetting;
import codefun.netty_only.util.NettyUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理前端的handler
 */
@Slf4j
public class WebSocketFrameHandler extends ChannelInboundHandlerAdapter {
    private final AppSetting appSetting;
    private Channel backendChannel;

    public WebSocketFrameHandler(AppSetting appSetting) {
        super();
        log.debug("WebSocketFrameHandler created");
        this.appSetting = appSetting;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Channel active {}", ctx.name());

        final var frontendChannel = ctx.channel();

        // 共享eventloop
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(frontendChannel.eventLoop())
                .channel((NioSocketChannel.class))
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new BackendHandler(frontendChannel));
                    }
                });

        log.debug("Connecting to backend {}:{}", appSetting.getBackendHost(), appSetting.getBackendPort());
        var f = bootstrap.connect(appSetting.getBackendHost(), appSetting.getBackendPort());
        backendChannel = f.channel();
        f.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                frontendChannel.read();
            } else {
                log.error("Failed to connect to backend", future.cause());
                frontendChannel.close();
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Channel inactive {}", ctx.name());
        if (backendChannel != null) {
            NettyUtil.closeOnFlush(backendChannel);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("Channel read {}", msg);
        if (msg instanceof TextWebSocketFrame textWebSocketFrame) {
            proxyRequest(textWebSocketFrame.content(), ctx);
        } else if (msg instanceof BinaryWebSocketFrame binFrame) {
            proxyRequest(binFrame.content(), ctx);
        } else {
            log.error("Unsupported msg type: {}", msg.getClass().getName());
        }
    }

    private void proxyRequest(ByteBuf data, ChannelHandlerContext ctx) {
        if (backendChannel != null && backendChannel.isActive()) {
            backendChannel.writeAndFlush(data).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    ctx.channel().read();
                } else {
                    log.error("Failed to write to backend", future.cause());
                    future.channel().close();
                }
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception caught", cause);
        NettyUtil.closeOnFlush(ctx.channel());
    }
}