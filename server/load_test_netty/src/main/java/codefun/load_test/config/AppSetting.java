package codefun.load_test.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppSetting {
    private String host;
    private int port;
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
     * for websocket
     */
    String webSocketPath;

    /**
     * user count
     */
    private int userCount;
    /**
     * ramp up seconds
     */
    int rampUpTime;

    public boolean isWebSocketMode() {
        return webSocketPath != null && !webSocketPath.isEmpty();
    }

}
