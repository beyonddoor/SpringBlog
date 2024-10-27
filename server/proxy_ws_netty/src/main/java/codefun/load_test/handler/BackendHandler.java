package codefun.load_test.handler;

import codefun.load_test.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理后端的handler
 */
@Slf4j
public class BackendHandler extends ChannelInboundHandlerAdapter {

    private final Channel frontendChannel;

    public BackendHandler(Channel frontendChannel) {
        this.frontendChannel = frontendChannel;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("Channel read {}", msg);
        if (frontendChannel.isActive()) {
            var buff = (ByteBuf) msg;
            frontendChannel.writeAndFlush(new BinaryWebSocketFrame(buff))
                    .addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            ctx.read();
                        } else {
                            future.channel().close();
                        }
                    });
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
