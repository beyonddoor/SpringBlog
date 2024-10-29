package codefun.load_test.logic.ai;

import codefun.load_test.logic.user.User;
import io.netty.buffer.ByteBuf;

public interface IUserAI {
    void setUser(User user);
    void start();
    void stop();

    void onRead(ByteBuf buf);
}
