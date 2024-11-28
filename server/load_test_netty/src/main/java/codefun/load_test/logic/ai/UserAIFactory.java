package codefun.load_test.logic.ai;

import codefun.load_test.logic.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserAIFactory {
    public IUserAI createStrategy(String strategyName, User user) {
        if (strategyName.equals("echo")) {
            return new UserAIEcho(user);
        }
        return null;
    }
}
