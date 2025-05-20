# Keep the main class

-keep public class codefun.proxy_ws_netty.ProxyApplication {
   public static void main(java.lang.String[]);
}

-keepnames class codefun.proxy_ws_netty.ProxyApplication


# Minify all classes in the codefun package
-keep class codefun.proxy_ws_netty.config.** {
   <fields>;
   <methods>;
}

# Keep Spring Boot loader classes
-keep class org.springframework.boot.loader.** {
    *;
}

-dontwarn **    # Ignore warnings