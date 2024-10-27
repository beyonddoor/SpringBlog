package codefun.load_test.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppSetting {
    private int userCount;
    private String host;
    private String message;
    private int port;
}
