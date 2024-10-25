package codefun.netty_only.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppSetting {
    private String backendHost;
    private int backendPort;
    private int port;
    private String webSocketPath;
}
