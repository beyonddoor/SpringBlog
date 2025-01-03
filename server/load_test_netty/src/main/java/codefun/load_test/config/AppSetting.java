package codefun.load_test.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Slf4j
@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppSetting {
    private String remoteAddress;
    /**
     * send message interval
     */
    private int messageIntervalMs;

    private int waitToReconnectMs;

    private int logStatIntervalMs;

    /**
     * send int count
     */
    int sendIntCount;

    /**
     * user count
     */
    private int userCount;
    /**
     * ramp up seconds
     */
    int rampUpTime;

    public SocketAddress getTargetAddress() {
        var addr = remoteAddress;
        if(addr.startsWith("ws://")) {
            addr = addr.substring(5);
        }
        var lastIndex = addr.indexOf("/");
        if(lastIndex != -1) {
            addr = addr.substring(0, lastIndex);
        }
        return parseAddr(addr);
    }

    private static SocketAddress parseAddr(String addr) {
        var parts = addr.split(":");
        if(parts.length != 2) {
            throw new IllegalArgumentException("remoteAddress format error");
        }
        return new InetSocketAddress(parts[0], Integer.parseInt(parts[1]));
    }

    public boolean isWebSocketMode() {
        return remoteAddress.startsWith("ws://");
    }
}
