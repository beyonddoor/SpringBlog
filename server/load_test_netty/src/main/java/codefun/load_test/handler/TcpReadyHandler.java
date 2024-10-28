package codefun.load_test.handler;

import codefun.load_test.logic.user.User;
import io.netty.channel.ChannelHandlerContext;

public class TcpReadyHandler extends ReadyHandlerBase {

    public TcpReadyHandler(User user) {
        super(user);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        user.getUserManager().onChannelReady(user);
    }
}
