package codefun.netty_only;

import codefun.netty_only.config.AppSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyOnlyApplication implements ApplicationRunner {
    @Autowired
    private AppSetting appSetting;

    public static void main(String[] args) {
        SpringApplication.run(NettyOnlyApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new WebSocketServer(appSetting).start();
    }
}
