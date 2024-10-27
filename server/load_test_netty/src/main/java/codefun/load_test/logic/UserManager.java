package codefun.load_test.logic;

import codefun.load_test.logic.ai.UserAIFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class UserManager {

    private final UserAIFactory strategyFactory;

    private final ConcurrentHashMap<ChannelId, User> channelMap = new ConcurrentHashMap<>();

    public UserManager(UserAIFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public void onChannelActive(Channel channel) {
        User user = new User(channel);

        var userAI = strategyFactory.createStrategy("echo");
        userAI.setUser(user);
        user.start(userAI);
        channelMap.put(user.channel().id(), user);
    }

    public void onChannelInactive(Channel channel) {
        var user = channelMap.remove(channel.id());
        if (user != null) {
            user.stop();
        }
    }

    public void logStat() {
        log.info("stat {}", channelMap.size());

    }
}
