package codefun.proxy_ws.util;

import org.springframework.core.env.ConfigurableEnvironment;

public class EnvDumper {
    public static void dumpEnv() {
        System.out.println("--> Environment Variables:");
        System.getenv().forEach((key, value) -> System.out.println(key + ": " + value));
    }

    // dump all properties
    public static void dumpProperties() {
        System.out.println("--> System Properties:");
        System.getProperties().forEach((key, value) -> System.out.println(key + ": " + value));
    }

    //TODO dump JNDI attributes
    public static void dumpJNDI() {
        //TODO
    }

    // dump all application properties
    public static void dumpAppProperties(ConfigurableEnvironment environment) {
        System.out.println("--> Dumping ConfigurableEnvironment:");

        // Iterate over all property names
        environment.getPropertySources().forEach(propertySource -> {
            System.out.println("---> Source: " + propertySource.getName() + " class " + propertySource.getClass());
            // 有一些实现没法遍历
            if (propertySource instanceof org.springframework.core.env.MapPropertySource mapPropertySource) {
                mapPropertySource.getSource().forEach((key, value) -> {
                    System.out.println(key + ": " + value);
                });
            }
        });

        System.out.println("--> getSystemEnvironment:");
        environment.getSystemEnvironment().forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });

        System.out.println("--> getSystemProperties:");
        environment.getSystemProperties().forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
    }

    public static void dumpAll() {
        dumpEnv();
        dumpProperties();
    }
}
