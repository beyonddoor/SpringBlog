package codefun.proxy_ws.component;


import codefun.proxy_ws.util.EnvDumper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class ProfileLogger {

    @Autowired
    private Environment environment;
    @Autowired
    private ConfigurableEnvironment configEnv;

    @PostConstruct
    public void logActiveProfiles() {
        String[] activeProfiles = environment.getActiveProfiles();
        System.out.println("Active Profiles: " + String.join(", ", activeProfiles));

        System.out.println(environment);
        System.out.println("configEnv " + configEnv);

        EnvDumper.dumpAll();
        EnvDumper.dumpAppProperties(configEnv);
    }
}