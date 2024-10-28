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
    private int userCount;
    private String host;
    /**
     * send text
     */
    private String message;
    /**
     * send file
     */
    private String messageFile;
    /**
     * send message interval
     */
    private int messageIntervalMs;

    private int port;

    /**
     * for websocket
     */
    String webSocketPath;

    /**
     * ramp up seconds
     */
    int rampUpTime;

    private static byte[] sendBytes = null;

    public byte[] getSendBytes() {
        if (sendBytes == null) {
            if (getMessageFile() != null) {
                try {
                    sendBytes = Files.readAllBytes(new File(getMessageFile()).toPath());
                } catch (IOException e) {
                    log.error("read file error", e);
                    throw new RuntimeException(e);
                }
            } else {
                sendBytes = getMessage().getBytes();
            }
        }
        return sendBytes;
    }
}
