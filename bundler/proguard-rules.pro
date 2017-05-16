-keep public class * implements com.hendraanggrian.bundler.BundleBinding {
    public <init>(**, android.os.Bundle);
    public <init>(java.util.List);
}

-keep class com.hendraanggrian.bundler.*
-keepclasseswithmembernames class * { @com.hendraanggrian.bundler.annotations.* <fields>; }