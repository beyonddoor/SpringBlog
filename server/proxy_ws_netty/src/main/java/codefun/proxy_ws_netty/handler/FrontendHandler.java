package codefun.proxy_ws_netty.handler;

import codefun.proxy_ws_netty.config.AppSetting;
import codefun.util.NettyUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理前端的handler
 */
@Slf4j
public class FrontendHandler extends ChannelInboundHandlerAdapter {
    private final AppSetting appSetting;
    /**
     * 和后端的channel
     */
    private Channel backendChannel;
    private final List<ByteBuf> pendingWrites = new ArrayList<>();

    public FrontendHandler(AppSetting appSetting) {
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
                        ch.pipeline().addLast(new BackendHandler(frontendChannel, appSetting));
                    }
                });

        log.debug("Connecting to backend {}:{}", appSetting.getBackendHost(), appSetting.getBackendPort());
        bootstrap.connect(appSetting.getBackendHost(), appSetting.getBackendPort())
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        backendChannel = future.channel();
                        // 发送累积的包
                        sendPendingPackets();
                        frontendChannel.read();
                    } else {
                        log.error("Failed to connect to backend", future.cause());
                        // 清除累积的包
                        clearPendingPackets();
                        frontendChannel.close();
                    }
                });
    }

    /**
     * 发送pending的数据
     */
    private void sendPendingPackets() {
        for (ByteBuf byteBuf : pendingWrites) {
            backendChannel.writeAndFlush(byteBuf).addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    log.error("Failed to write to backend", future.cause());
                    future.channel().close();
                }
            });
        }
    }

    private void clearPendingPackets() {
        for (ByteBuf byteBuf : pendingWrites) {
            byteBuf.release();
        }
        pendingWrites.clear();
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
        if(log.isDebugEnabled() && appSetting.isLogData()) {
            log.debug("Channel read {}", msg);
        }

        if (msg instanceof TextWebSocketFrame textWebSocketFrame) {
            proxyRequest(textWebSocketFrame, ctx);
        } else if (msg instanceof BinaryWebSocketFrame binaryWebSocketFrame) {
            proxyRequest(binaryWebSocketFrame, ctx);
        } else if(msg instanceof PingWebSocketFrame || msg instanceof PongWebSocketFrame) {
            // ignore
        } else {
            log.error("Unsupported msg type: {}", msg.getClass().getName());
            NettyUtil.closeOnFlush(ctx.channel());
        }
    }

    private void proxyRequest(ByteBufHolder byteBufHolder, ChannelHandlerContext ctx) {
        if (backendChannel != null && backendChannel.isActive()) {
            backendChannel.writeAndFlush(byteBufHolder.content()).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    ctx.channel().read();
                } else {
                    log.error("Failed to write to backend", future.cause());
                    future.channel().close();
                }
            });
        } else {
            pendingWrites.add(byteBufHolder.content());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception caught", cause);
        NettyUtil.closeOnFlush(ctx.channel());
    }
}