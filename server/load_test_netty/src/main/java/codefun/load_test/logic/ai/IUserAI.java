package codefun.load_test.logic.ai;

import codefun.load_test.logic.user.User;

public interface IUserAI {
    void setUser(User user);
    void start();
    void stop();
}
