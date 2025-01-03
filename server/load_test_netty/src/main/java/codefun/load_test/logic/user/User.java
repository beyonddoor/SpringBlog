package codefun.load_test.logic.user;


import codefun.load_test.logic.ai.IUserAI;
import codefun.load_test.logic.ai.UserAIFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class User implements IUser{
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

    private void startAI(IUserAI userAI) {
        destroyAI();
        this.userAI = userAI;
        userAI.onStart();
    }

    public void stop() {
        destroyAI();

        if (connector != null) {
            connector.stop();
            connector = null;
        }

        userState = UserState.DISCONNECTED;
    }

    public void start() {
        log.debug("User start {}", this);

        stop();

        try {
            connector = new Connector(this);
            channelFuture = connector.start();
            userState = UserState.CONNECTING;
        } catch (Exception e) {
            log.error("connector start error", e);
        }
    }

    public void onDisconnected() {
        userState = UserState.DISCONNECTED;
    }

    public void onConnected() {
        userState = UserState.READY;
        startAI(strategyFactory.createStrategy("echo", this));
    }

    public void onChannelRead(ByteBuf buf) {
        if (userAI != null) {
            userAI.onRead(buf);
        } else {
            buf.release();
        }
    }

    public void destroy() {
        stop();
        destroyAI();
    }

    private void destroyAI() {
        if(userAI != null) {
            userAI.onDestroy();
            userAI = null;
        }
    }

    public void logStat() {
        if (userAI != null) {
            userAI.logStat();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }
}
