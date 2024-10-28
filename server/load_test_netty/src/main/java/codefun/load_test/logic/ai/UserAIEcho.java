package codefun.load_test.logic.ai;

import codefun.load_test.config.AppSetting;
import codefun.load_test.logic.user.User;
import codefun.load_test.util.SpringContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class UserAIEcho implements IUserAI {

    private User user;
    private ScheduledFuture<?> future;
    private ByteBuf byteBuf;
    private final AppSetting appSetting;

    public UserAIEcho() {
        appSetting = SpringContext.getBean(AppSetting.class);
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void start() {
        log.debug("UserAIEcho start");

        if (byteBuf == null) {
            byteBuf = user.channel().alloc().directBuffer();
            assert byteBuf.refCnt() == 1;
        }

        future = user.channel().eventLoop().scheduleAtFixedRate(
                this::sendMsg, 1, appSetting.getMessageIntervalMs(), TimeUnit.MILLISECONDS);
    }

    private ByteBuf getBuf() {
        byteBuf.resetReaderIndex();
        byteBuf.resetWriterIndex();
        return byteBuf;
    }

    private void sendMsg() {
        log.debug("UserAIEcho sendMsg");

        assert getBuf().refCnt() == 1;
        var data = getBuf().retain().writeBytes(appSetting.getSendBytes());
        if (appSetting.getWebSocketPath() != null) {
            user.channel().writeAndFlush(new BinaryWebSocketFrame(data));
        } else {
            user.channel().writeAndFlush(data);
        }
    }

    @Override
    public void stop() {
        log.debug("UserAIEcho stop");
        if (byteBuf != null) {
            byteBuf.release();
            byteBuf = null;
        }
        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }
}
