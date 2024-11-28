package codefun.load_test;

import codefun.load_test.config.AppSetting;
import codefun.load_test.logic.user.IUser;
import codefun.load_test.logic.user.IUserListener;
import codefun.load_test.logic.user.User;
import codefun.load_test.logic.user.UserManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"codefun.load_test", "codefun.util"})
public class LoadTestApplication implements ApplicationRunner {
    @Getter
    private final AppSetting appSetting;
    private final UserManager userManager;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public LoadTestApplication(AppSetting appSetting, UserManager userManager) {
        this.appSetting = appSetting;
        this.userManager = userManager;
    }

    private void shutdown() {
        scheduler.shutdown();
        userManager.destroyAllUsers();
    }

    public static void main(String[] args) {
        SpringApplication.run(LoadTestApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        scheduler.scheduleAtFixedRate(userManager::logStat, 0,
                appSetting.getLogStatIntervalMs(), TimeUnit.MILLISECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        userManager.addUserListener(new IUserListener() {
            @Override
            public void onUserDisconnected(IUser user) {
                scheduler.schedule(user::start, appSetting.getWaitToReconnectMs(), TimeUnit.MILLISECONDS);
            }
        });

        // get high resolution time
        var startTime = new Date();
        var lastTimeNs = System.nanoTime();
        int batchCount = 10;
        for (int i = 1; i <= appSetting.getUserCount(); i++) {

            var user = userManager.createUser();
            user.start();

            if (i % batchCount == 0) {
                var diffNs = System.nanoTime() - lastTimeNs;
                lastTimeNs = System.nanoTime();
                var sleepMs = (int) ((float) batchCount * appSetting.getRampUpTime() / appSetting.getUserCount() * 1000);
                // todo more accurate ramp up time
                // sleepMs -= (int)(diffNs / 1000000);
                if (sleepMs > 0) {
                    Thread.sleep(sleepMs);
                }
            }
        }

        log.info("start {} users in {}", appSetting.getUserCount(), new Date().getTime() - startTime.getTime());

        while (true) {
            Thread.sleep(1000);
        }
    }
}
