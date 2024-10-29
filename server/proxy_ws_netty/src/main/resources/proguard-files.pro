# Keep the main class

-keep public class codefun.ws_demo.WsApplication {
   public static void main(java.lang.String[]);
}

# Minify all classes in the codefun package
-keep class codefun.** {
   <fields>;
   <methods>;
}

-dontwarn **    # Ignore warnings