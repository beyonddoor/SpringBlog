package codefun.load_test.logic.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserAIFactory {
    public IUserAI createStrategy(String strategyName) {
        if (strategyName.equals("echo")) {
            return new UserAIEcho();
        }
        return null;
    }
}
