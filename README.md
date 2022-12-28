[![Travis CI](https://img.shields.io/travis/com/hendraanggrian/auto-bundler)](https://travis-ci.com/github/hendraanggrian/auto-bundler/)
[![Codecov](https://img.shields.io/codecov/c/github/hendraanggrian/auto-bundler)](https://codecov.io/gh/hendraanggrian/auto-bundler/)
[![Maven Central](https://img.shields.io/maven-central/v/com.hendraanggrian.auto/bundler)](https://search.maven.org/artifact/com.hendraanggrian.auto/bundler/)
[![Nexus Snapshot](https://img.shields.io/nexus/s/com.hendraanggrian.auto/bundler?server=https%3A%2F%2Fs01.oss.sonatype.org)](https://s01.oss.sonatype.org/content/repositories/snapshots/com/hendraanggrian/auto/bundler/)
[![Android SDK](https://img.shields.io/badge/sdk-14%2B-informational)](https://developer.android.com/studio/releases/platforms/#4.0)

# Auto Bundler

Passing key-value pairs with Bundle is the most common approach of exchanging
data across Android components. Unfortunately, much of the process of sending
and receiving those key-value pairs (known as extra) requires a lot of
boilerplate code. Auto Bundler aims to minify the process with annotation
processing.

```java
public class ExampleActivity extends Activity {
    @Extra String username;
    @State int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundler.bindExtras(this);
        Bundler.bindStates(this, savedInstanceState);
        // TODO: Use fields...
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundler.saveStates(this, outState);
    }
}
```

## Download

```gradle
repositories {
    mavenCentral()
}
dependencies {
    implementation "com.hendraanggrian.auto:bundle:$version"
    annotationProcessor "com.hendraanggrian.auto:bundle-compiler:$version" // or kapt for Kotlin
}
```

## Usage

### Extra binding

#### Binding

`@BindExtra` for binding extra value to field, field cannot be private. When key
is not provided, field name will be used as the key.

```kotlin
@BindExtra @JvmField var username: String
```

#### Wrapping

Create extras with varargs argument with `extrasOf()`. This is optional, any
Bundle would work just fine.

```java
Intent intent = new Intent(context, ExampleActivity.class);
intent.putExtras(Bundler.extrasOf(ExampleActivity.class, "Hendra Anggrian", 24));
startActivity(intent);
```

### State binding

#### Restoring

`@BindState` for binding extra value to field, field cannot be private. When key
 is not provided, field name will be used as the key.

```kotlin
@BindExtra @JvmField var position: Int

override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    restoreStates(savedInstanceState)
}
```

#### Saving

Simply call `Bundle.saveStates()` to save states.

```kotlin
override fun onSaveInstanceState(outState: Bundle) {
   super.onSaveInstanceState(outState)
   saveStates(outState)
}
```

## Supported extra types

- primitive data types and array of them.
- `CharSequence`, `CharSequence[]`, and `ArrayList<CharSequence>`
- `String`, `String[]`, and `ArrayList<String>`
- `Parcelable`, `Parcelable[]`, `ArrayList<Parcelable>` and `SparseArray<Parcelable>`
- `Serializable`

## Parceler

[Parceler][parceler] is supported with this library, it is a library that easily
makes any object implements Parcelable with generated code, making it able to be
inserted to Bundle as Parcelable.

`Bundler.wrap()` automatically converts the object to Parcelable.
Without `Bundler.wrap()`, object must be wrapped using `Parcels.wrap()`. Head
to [Parceler doc][parceler] for more information.

```java
User user = new User("Hendra Anggrian", 24);
// with Bundler.wrap()
intent.putExtras(Bundler.wrap(UserActivity.class, user));
// without Bundler.wrap()
intent.putExtra("user", Parcels.wrap(user));
```

`Bundler.bind()` automatically converts the Parcelable back to original object.

```kotlin
class UserActivity : Activity {
    @Extra("user") lateinit var user : User

    override fun onCreate(savedInstanceState : Bundle) {
        super.onCreate(savedInstanceState)
        bindExtras()
    }
}
```

## Optional bindings

Extra bindings are required by default, an exception will be thrown if key is
not found in Bundle. If this is not a desired behavior, annotate the field
with `@Nullable` from [Android's support design library][support-annotations].

```java
@Nullable @BindExtra String username;
```
