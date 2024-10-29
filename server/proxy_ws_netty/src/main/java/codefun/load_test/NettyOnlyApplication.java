package codefun.load_test;

import codefun.load_test.config.AppSetting;
import codefun.load_test.handler.CounterHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

@Slf4j
@SpringBootApplication
public class NettyOnlyApplication implements ApplicationRunner {

    private final AppSetting appSetting;
    @Autowired
    private CounterHandler counterHandler;
    ExecutorService executor = Executors.newFixedThreadPool(1);

    public NettyOnlyApplication(AppSetting appSetting) {
        this.appSetting = appSetting;
    }

    public static void main(String[] args) {
        SpringApplication.run(NettyOnlyApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
        }));

        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    log.info("connections: {}", counterHandler.getCounter());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        new WebSocketServer(appSetting, counterHandler).start();
    }
}
