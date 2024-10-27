package codefun.load_test.handler;

import codefun.load_test.logic.UserManager;
import codefun.load_test.config.AppSetting;
import codefun.load_test.util.NettyUtil;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理前端的handler
 */
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private final AppSetting appSetting;
    private final UserManager userManager;

    public ClientHandler(AppSetting appSetting, UserManager userManager) {
        super();
        log.debug("ClientHandler created");
        this.appSetting = appSetting;
        this.userManager = userManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelActive {}", ctx.channel());
        userManager.onChannelActive(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelInactive {}", ctx.channel());
        userManager.onChannelInactive(ctx.channel());
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