package codefun.echoserver.config;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "app")
@Component
@Data
public class EchoServerConfig {
    private int localPort;
}
