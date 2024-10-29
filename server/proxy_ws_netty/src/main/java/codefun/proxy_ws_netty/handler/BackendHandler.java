package codefun.proxy_ws_netty.handler;

import codefun.proxy_ws_netty.config.AppSetting;
import codefun.proxy_ws_netty.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理后端的handler
 */
@Slf4j
public class BackendHandler extends ChannelInboundHandlerAdapter {
    /**
     * 前端的channel
     */
    private final Channel frontendChannel;
    private final AppSetting appSetting;

    public BackendHandler(Channel frontendChannel, AppSetting appSetting) {
        this.frontendChannel = frontendChannel;
        this.appSetting = appSetting;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (log.isDebugEnabled() && appSetting.isLogData()) {
            log.debug("Channel read {}", msg);
        }

        var buff = (ByteBuf) msg;
        if (frontendChannel.isActive()) {
            frontendChannel.writeAndFlush(new BinaryWebSocketFrame(buff))
                    .addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            ctx.read();
                        } else {
                            future.channel().close();
                        }
                    });
        } else {
            buff.release();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Channel inactive {}", ctx.name());
        // close frontend
        NettyUtil.closeOnFlush(frontendChannel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception caught {}", ctx.name(), cause);
        NettyUtil.closeOnFlush(ctx.channel());
    }
}
