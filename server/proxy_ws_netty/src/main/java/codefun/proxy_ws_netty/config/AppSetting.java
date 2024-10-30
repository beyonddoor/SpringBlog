package codefun.proxy_ws_netty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppSetting {
    private String backendHost;
    private int backendPort;

    private int localPort;
    private String localAddr;

    private String webSocketPath;
    private boolean logData;

    public InetSocketAddress getAddress() {
        if(localAddr == null || localAddr.isEmpty()) {
            return new InetSocketAddress(localPort);
        }
        return new InetSocketAddress(localAddr, localPort);
    }
}
