package codefun.load_test.logic.ai;

import codefun.load_test.config.AppSetting;
import codefun.load_test.logic.user.User;
import codefun.util.SpringContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class UserAIEcho implements IUserAI {
    private User user;
    private ScheduledFuture<?> future;
    private final AppSetting appSetting;
    private final ByteBuf bufToWrite;
    private final ByteBuf bufToRead;

    private long sendSeq = 0;
    private long recvSeq = 0;

    public UserAIEcho() {
        appSetting = SpringContext.getBean(AppSetting.class);
        bufToWrite = Unpooled.directBuffer();
        bufToRead = Unpooled.directBuffer();
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void onStart() {
        log.debug("{} UserAIEcho start", user);

        future = user.channel().eventLoop().scheduleAtFixedRate(
                this::sendMsg, 1, appSetting.getMessageIntervalMs(), TimeUnit.MILLISECONDS);
    }

    private ByteBuf getBuf() {
        bufToWrite.resetReaderIndex();
        bufToWrite.resetWriterIndex();
        return bufToWrite;
    }

    private void sendMsg() {
        assert getBuf().refCnt() == 1;

        //fixme: sendSeq is not reset, may be this is run in another thread
        var data = getBuf().retain();
        for (int i = 0; i < appSetting.getSendIntCount(); i++) {
            sendSeq++;
            data.writeLong(sendSeq);
        }
        log.debug("{} UserAIEcho sendMsg sendSeq={} recvSeq={}", user, sendSeq, recvSeq);
        doSend(data);
    }

    private void doSend(ByteBuf data) {
        if (appSetting.isWebSocketMode()) {
            user.channel().writeAndFlush(new BinaryWebSocketFrame(data));
        } else {
            user.channel().writeAndFlush(data);
        }
    }

    @Override
    public void onStop() {
        log.debug("{} UserAIEcho stop sendSeq={} recvSeq={}", user, sendSeq, recvSeq);

        if (future != null) {
            future.cancel(true);
            future = null;
        }

        sendSeq = 0;
        recvSeq = 0;
    }

    @Override
    public void onRead(ByteBuf buf) {
        bufToRead.writeBytes(buf);
        buf.release();

        while (bufToRead.readableBytes() >= Long.BYTES) {
            long seq = bufToRead.readLong();
            if (seq != recvSeq + 1) {
                log.error("{} seq error {} {}", user, seq, recvSeq);
                user.stop();
                break;
            }
            recvSeq = seq;
        }

        log.debug("{} UserAIEcho onRead sendSeq={} recvSeq={}", user, sendSeq, recvSeq);
    }

    @Override
    public void onDestroy() {
        bufToWrite.release();
        bufToRead.release();
    }

    @Override
    public void logStat() {
        log.info("{} UserAIEcho status sendSeq={} recvSeq={}", user, sendSeq, recvSeq);
    }
}
