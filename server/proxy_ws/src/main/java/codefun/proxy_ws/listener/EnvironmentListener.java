package codefun.proxy_ws.listener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Component
public class EnvironmentListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();

        // Dump active profiles
        System.out.println("Active Profiles: " + Arrays.toString(environment.getActiveProfiles()));

        // Dump all Environment Variables
        System.out.println("Environment Variables:");
        Map<String, String> env = System.getenv();
        env.forEach((key, value) -> System.out.println(key + ": " + value));

        // Dump log levels for specific packages
        String logLevelCodefun = environment.getProperty("logging.level.codefun");
        String logLevelRoot = environment.getProperty("logging.level.root");

        System.out.println("Logging Level for package 'codefun': " + logLevelCodefun);
        System.out.println("Root Logging Level: " + logLevelRoot);
    }
}