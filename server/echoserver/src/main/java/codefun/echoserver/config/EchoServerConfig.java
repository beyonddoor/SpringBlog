package codefun.echoserver.config;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.InetSocketAddress;


@ConfigurationProperties(prefix = "app")
@Component
@Data
public class EchoServerConfig {
    private int localPort;
    private String localAddr;

    public InetSocketAddress getAddress() {
        if(localAddr == null || localAddr.isEmpty()) {
            return new InetSocketAddress(localPort);
        }
        return new InetSocketAddress(localAddr, localPort);
    }
}
