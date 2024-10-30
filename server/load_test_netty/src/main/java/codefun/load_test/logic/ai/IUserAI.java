package codefun.load_test.logic.ai;

import codefun.load_test.logic.user.User;
import io.netty.buffer.ByteBuf;

public interface IUserAI {
    void setUser(User user);
    void onStart();
    void onStop();

    void onRead(ByteBuf buf);
    void onDestroy();
    void logStat();
}
