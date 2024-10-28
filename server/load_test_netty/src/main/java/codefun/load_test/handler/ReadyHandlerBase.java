package codefun.load_test.handler;

import codefun.load_test.logic.user.User;
import codefun.load_test.util.NettyUtil;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理前端的handler
 */
@Slf4j
public abstract class ReadyHandlerBase extends ChannelInboundHandlerAdapter {
    protected final User user;

    public ReadyHandlerBase(User user) {
        super();
        log.debug("ClientHandler created");
        this.user = user;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelActive {}", ctx.channel());
        user.getUserManager().onChannelActive(user);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelInactive {}", ctx.channel());
        user.getUserManager().onChannelInactive(user);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("channelRead {}", msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelReadComplete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception caught", cause);
        NettyUtil.closeOnFlush(ctx.channel());
    }
}