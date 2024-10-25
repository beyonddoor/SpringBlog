package codefun.echoserver.handler;

import codefun.echoserver.Connector;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private ChannelFuture channelFuture;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Channel active");
        channelFuture = Connector.getInstance().connect("172.16.200.96", 22, ctx.channel());
        if (channelFuture == null) {
            log.error("Failed to connect to remote server");
            ctx.disconnect();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Channel inactive");
        if (channelFuture != null) {
            channelFuture.channel().close();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.debug("Server received: {}", msg);
        channelFuture.channel().write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        log.debug("Channel read complete");
        channelFuture.channel().flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        log.error("Exception caught", cause);
        ctx.close();
    }
}