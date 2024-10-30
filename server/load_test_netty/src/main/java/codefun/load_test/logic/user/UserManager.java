package codefun.load_test.logic.user;

import codefun.load_test.logic.ai.UserAIFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class UserManager {

    private static long nextId = 0;

    private final UserAIFactory strategyFactory;

    private final ArrayList<IUserListener> userListeners = new ArrayList<>();

    private final ConcurrentHashMap<Long, User> usersMap = new ConcurrentHashMap<>();

    private final HashMap<UserState, Integer> userStateMap = new HashMap<>();

    private final ArrayList<User> users = new ArrayList<>();

    public UserManager(UserAIFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public void onChannelActive(User user) {
        user.onChannelActive();
    }

    public void onChannelReady(User user) {
        user.onReady();

        for (var listener : userListeners) {
            listener.onUserStart(user);
        }
    }

    public void onChannelInactive(User user) {
        user.onDisconnected();

        for (var listener : userListeners) {
            listener.onUserStop(user);
        }
    }

    public void logStat() {
        userStateMap.clear();

        users.clear();
        users.addAll(usersMap.values());

        for (var user : users) {
            var state = user.getUserState();
            userStateMap.put(state, userStateMap.getOrDefault(state, 0) + 1);
        }

        var sb = new StringBuffer();
        for (var entry : userStateMap.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append(" ");
        }
        log.info("total:{} {}", usersMap.size(), sb);
        for (var user : users) {
            user.logStat();
        }
    }

    public void addUserListener(IUserListener listener) {
        userListeners.add(listener);
    }

    public void removeUserListener(IUserListener listener) {
        userListeners.remove(listener);
    }

    public User createUser() {
        User user = new User( this, strategyFactory);
        user.connect();
        usersMap.put(user.getId(), user);
        return user;
    }

    public void destroyUser(User user) {
        user.destroy();
        usersMap.remove(user.getId());
    }

    public long nextId() {
        return nextId++;
    }

    public void destroyAll() {
        for (var user : usersMap.values()) {
            user.destroy();
        }
        usersMap.clear();
    }
}
