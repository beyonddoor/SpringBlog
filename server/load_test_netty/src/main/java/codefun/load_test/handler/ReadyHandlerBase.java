package codefun.load_test.handler;

import codefun.load_test.logic.user.User;
import codefun.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
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
        log.debug("{} ClientHandler created", user);
        this.user = user;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("{} channelActive {}", user, ctx.channel());
        user.getUserManager().onChannelActive(user);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("{} channelInactive {}", user, ctx.channel());
        user.getUserManager().onChannelInactive(user);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("{} channelRead {}", user, msg);
        if(msg instanceof ByteBufHolder holder) {
            ByteBuf buf = holder.content();
            user.onChannelRead(buf);
        } else if (msg instanceof ByteBuf buf) {
            user.onChannelRead(buf);
        } else {
            log.error("Unknown message type: {}", msg.getClass());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.debug("{} channelReadComplete", user);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("{} Exception caught", user, cause);
        NettyUtil.closeOnFlush(ctx.channel());
    }
}