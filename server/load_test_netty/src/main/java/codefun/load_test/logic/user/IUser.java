package codefun.load_test.logic.user;

import io.netty.channel.Channel;

public interface IUser {

    Channel channel();

    void start();
    void stop();
    void destroy();

    long getId();
}
