package codefun.load_test.logic.ai;

import codefun.load_test.config.AppSetting;
import codefun.load_test.logic.User;
import codefun.load_test.util.SpringContext;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class UserAIEcho implements IUserAI {

    private User user;
    private ScheduledFuture<?> future;
    private ByteBuf byteBuf;
    private final String sendMessage;

    public UserAIEcho() {
        sendMessage = SpringContext.getBean(AppSetting.class).getMessage();
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void start() {
        log.debug("UserAIEcho start");

        if(byteBuf == null) {
            byteBuf = user.channel().alloc().buffer();
        }

        future = user.channel().eventLoop().scheduleAtFixedRate(() -> {
            user.channel().writeAndFlush(getBuf().writeBytes(sendMessage.getBytes()));
        }, 1, 1, TimeUnit.SECONDS);
    }

    private ByteBuf getBuf() {
        byteBuf.resetReaderIndex();
        byteBuf.resetWriterIndex();
        byteBuf.retain();
        return byteBuf;
    }

    @Override
    public void stop() {
        log.debug("UserAIEcho stop");
        if(byteBuf != null) {
            byteBuf.release();
        }
        if (future != null) {
            future.cancel(true);
        }
    }
}
