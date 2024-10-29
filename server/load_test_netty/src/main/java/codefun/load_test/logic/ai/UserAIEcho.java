package codefun.load_test.logic.ai;

import codefun.load_test.config.AppSetting;
import codefun.load_test.logic.user.User;
import codefun.load_test.util.SpringContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class UserAIEcho implements IUserAI {
    private User user;
    private ScheduledFuture<?> future;
    private final AppSetting appSetting;
    private ByteBuf bufToWrite;
    private ByteBuf bufToRead;

    private long sendSeq = 0;
    private long recvSeq = 0;

    public UserAIEcho() {
        appSetting = SpringContext.getBean(AppSetting.class);
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void start() {
        log.debug("{} UserAIEcho start", user);

        if (bufToWrite == null) {
            bufToWrite = user.channel().alloc().directBuffer();
            assert bufToWrite.refCnt() == 1;
        }

        if(bufToRead == null) {
            bufToRead = user.channel().alloc().directBuffer();
        }

        future = user.channel().eventLoop().scheduleAtFixedRate(
                this::sendMsg, 1, appSetting.getMessageIntervalMs(), TimeUnit.MILLISECONDS);
    }

    private ByteBuf getBuf() {
        bufToWrite.resetReaderIndex();
        bufToWrite.resetWriterIndex();
        return bufToWrite;
    }

    private boolean isWebSocket() {
        return appSetting.getWebSocketPath() != null;
    }

    private void sendMsg() {
        log.debug("UserAIEcho sendMsg");

        assert getBuf().refCnt() == 1;

        var data = getBuf().retain();
        for (int i = 0; i < appSetting.getSendIntCount(); i++) {
            sendSeq++;
            data.writeLong(sendSeq);
        }

        doSend(data);
    }

    private void doSend(ByteBuf data) {
        if (isWebSocket()) {
            user.channel().writeAndFlush(new BinaryWebSocketFrame(data));
        } else {
            user.channel().writeAndFlush(data);
        }
    }

    @Override
    public void stop() {
        log.debug("{} UserAIEcho stop sendSeq={} recvSeq={}", user, sendSeq, recvSeq);
        if (bufToWrite != null) {
            bufToWrite.release();
            bufToWrite = null;
        }
        if (bufToRead != null) {
            bufToRead.release();
            bufToRead = null;
        }
        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }

    @Override
    public void onRead(ByteBuf buf) {

        bufToRead.writeBytes(buf);
        buf.release();

        while (bufToRead.readableBytes() >= Long.BYTES) {
            long seq = bufToRead.readLong();
            if (seq != recvSeq + 1) {
                log.error("id:{} seq error {} {}", user.getId(), seq, recvSeq);
                user.stop();
                break;
            }
            recvSeq = seq;
        }
    }
}
