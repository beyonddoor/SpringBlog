package codefun.load_test.logic.user;


import codefun.load_test.logic.ai.IUserAI;
import codefun.load_test.logic.ai.UserAIFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class User {
    private IUserAI userAI;
    private final UserAIFactory strategyFactory;

    @Getter
    private UserState userState = UserState.DISCONNECTED;

    private ChannelFuture channelFuture;
    private Connector connector;

    @Getter
    private final UserManager userManager;

    @Getter
    private long id;

    public User(UserManager userManager, UserAIFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
        this.userManager = userManager;
        this.id = userManager.nextId();
    }

    public Channel channel() {
        return channelFuture.channel();
    }

    public void start(IUserAI userAI) {
        this.userAI = userAI;
        userAI.start();
    }

    public void stop() {
        if(userAI != null) {
            userAI.stop();
            userAI = null;
        }

        if(connector != null) {
            connector.stop();
            connector = null;
        }

        userState = UserState.DISCONNECTED;
    }

    public void connect() {
        stop();

        try {
            connector = new Connector(this);
            channelFuture = connector.start();
            userState = UserState.CONNECTING;
        } catch (Exception e) {
            log.error("connector start error", e);
        }
    }

    public void onConnectFailed() {
        userState = UserState.DISCONNECTED;
        getUserManager().onChannelInactive(this);
    }

    public void onConnected() {
        userState = UserState.CONNECTED;
    }

    public void onDisconnected() {
        stop();
    }

    public void onReady() {
        userState = UserState.READY;
        var userAI = strategyFactory.createStrategy("echo");
        userAI.setUser(this);
        start(userAI);
    }
}
