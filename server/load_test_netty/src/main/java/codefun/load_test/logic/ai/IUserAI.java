package codefun.load_test.logic.ai;

import codefun.load_test.logic.user.User;
import io.netty.buffer.ByteBuf;

public interface IUserAI {
    /**
     * ai的启动
     */
    void onStart();

    /**
     * ai的停止
     */
    void onDestroy();

    /**
     * 读取数据
     * @param buf
     */
    void onRead(ByteBuf buf);

    /**
     * 输出统计
     */
    void logStat();
}
