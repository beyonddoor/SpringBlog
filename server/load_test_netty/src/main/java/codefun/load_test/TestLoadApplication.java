package codefun.load_test;

import codefun.load_test.config.AppSetting;
import codefun.load_test.logic.UserManager;
import codefun.load_test.service.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@SpringBootApplication
public class TestLoadApplication implements ApplicationRunner {
    private final AppSetting appSetting;
    private final UserManager userManager;
    private final Connector connector;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public TestLoadApplication(AppSetting appSetting, UserManager userManager, Connector connector) {
        this.appSetting = appSetting;
        this.userManager = userManager;
        this.connector = connector;
    }

    public static void main(String[] args) {
        SpringApplication.run(TestLoadApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for(int i = 0; i < appSetting.getUserCount(); i++) {
            connector.start();
        }

        scheduler.scheduleAtFixedRate(userManager::logStat, 0, 1, TimeUnit.SECONDS);

        while (true) {
            Thread.sleep(1000);
        }
    }
}
