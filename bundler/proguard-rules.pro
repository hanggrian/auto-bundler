# library
-keep class com.hendraanggrian.auto.bundler.*

# generated classes
-keep public class * extends com.hendraanggrian.auto.bundler.internal.BundleBinding {
    public <init>(**, android.os.Bundle); # extras and states binding
    public <init>(android.os.Bundle, **); # states saving
    public <init>(java.util.List); # extras wrapping
}

# annotated fields
-keepclasseswithmembernames class * { @com.hendraanggrian.auto.bundler.BindExtra <fields>; }
-keepclasseswithmembernames class * { @com.hendraanggrian.auto.bundler.BindState <fields>; }
