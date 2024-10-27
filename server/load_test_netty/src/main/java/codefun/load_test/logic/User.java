package codefun.load_test.logic;


import codefun.load_test.logic.ai.IUserAI;
import io.netty.channel.Channel;

public class User {
    private Channel channel;
    private IUserAI userAI;

    public User(Channel channel) {
        this.channel = channel;
    }

    public Channel channel() {
        return channel;
    }

    public void start(IUserAI userAI) {
        this.userAI = userAI;
        userAI.start();
    }

    public void stop() {
        userAI.stop();
    }
}
