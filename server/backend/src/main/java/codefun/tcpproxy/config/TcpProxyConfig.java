package codefun.tcpproxy.config;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "app")
@Component
@Data
public class TcpProxyConfig {
    private String host;
    private int port;
    private int localPort;
}
