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
    private final User user;
    private ScheduledFuture<?> future;
    private final AppSetting appSetting;
    private final ByteBuf bufToWrite;
    private final ByteBuf bufToRead;

    /**
     * 上次send的数字
     */
    private long lastSendNum = 0;
    private long lastRecvNum = 0;

    public UserAIEcho(User user) {
        appSetting = SpringContext.getBean(AppSetting.class);
        bufToWrite = Unpooled.directBuffer();
        bufToRead = Unpooled.directBuffer();
        this.user = user;
    }

    @Override
    public void onStart() {
        log.debug("{} UserAIEcho start", user);

        future = user.channel().eventLoop().scheduleAtFixedRate(
                this::sendMsg, 1, appSetting.getMessageIntervalMs(), TimeUnit.MILLISECONDS);
    }

    private void resetReadAndWriteBuff() {
        bufToWrite.resetReaderIndex();
        bufToWrite.resetWriterIndex();
    }

    private void sendMsg() {
        resetReadAndWriteBuff();
        assert bufToWrite.refCnt() == 1;

        //fixme: sendSeq is not reset, may be this is run in another thread
        var data = bufToWrite.retain();

        for (int i = 0; i < appSetting.getSendIntCount(); i++) {
            lastSendNum++;
            data.writeLong(lastSendNum);
        }
        log.debug("{} UserAIEcho sendMsg lastSendNum={} lastRecvNum={} sendBuff={}",
                user, lastSendNum, lastRecvNum, data.readableBytes());
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
    public void onRead(ByteBuf buf) {
        bufToRead.writeBytes(buf);
        buf.release();

        while (bufToRead.readableBytes() >= Long.BYTES) {
            long curReadNum = bufToRead.readLong();
            if (curReadNum != lastRecvNum + 1) {
                log.error("{} seq error lastSendNum={} curReadNum={} lastRecvNum={}",
                        user, lastSendNum, curReadNum, lastRecvNum);
                user.stop();
                break;
            }
            lastRecvNum = curReadNum;
        }

        log.debug("{} UserAIEcho onRead lastSendNum={} lastRecvNum={}", user, lastSendNum, lastRecvNum);
    }

    @Override
    public void onDestroy() {
        log.debug("{} UserAIEcho stop lastSendNum={} lastRecvNum={}", user, lastSendNum, lastRecvNum);

        if (future != null) {
            future.cancel(true);
            future = null;
        }

        bufToWrite.release();
        bufToRead.release();
    }

    @Override
    public void logStat() {
        log.info("{} UserAIEcho status lastSendNum={} lastRecvNum={} sendBuff={}",
                user, lastSendNum, lastRecvNum, bufToWrite.readableBytes());
    }
}
