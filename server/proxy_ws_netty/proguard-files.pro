# Keep your main class
-keep public class com.example.Main {
   public static void main(java.lang.String[]);
}

# Minify your classes
-keep class * {
   <fields>;
   <methods>;
}

-dontwarn **    # Ignore warnings