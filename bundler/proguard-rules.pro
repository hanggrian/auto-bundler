# Retain generated classes
-keep public class * extends BundleBinding {
    public <init>(**, android.os.Bundle);   # extras and states binding
    public <init>(android.os.Bundle, **);   # states saving
    public <init>(java.util.List);          # extras wrapping
}
# Retain library classes and fields marked with annotations from the library
-keep class com.hendraanggrian.bundler.*
-keepclasseswithmembernames class * { @com.hendraanggrian.bundler.* <fields>; }