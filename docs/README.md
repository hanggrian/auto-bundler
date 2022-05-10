[![version](https://img.shields.io/maven-central/v/com.hendraanggrian.auto/bundles)](https://search.maven.org/artifact/com.hendraanggrian.auto/bundles)
[![build](https://img.shields.io/travis/com/hendraanggrian/bundles)](https://www.travis-ci.com/github/hendraanggrian/bundles)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081)](https://ktlint.github.io)

Bundles
=======

Passing key-value pairs with Bundle is the most common approach of exchanging data across Android components.
Unfortunately, much of the process of sending and receiving those key-value pairs (known as extra) requires a lot of boilerplate code.
Bundles aims to minify the process with annotation processing.

```java
public class ExampleActivity extends Activity {
    @Extra String username;
    @State int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundles.bindExtras(this);
        Bundles.bindStates(this, savedInstanceState);
        // TODO: Use fields...
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundles.saveStates(this, outState);
    }
}
```

Download
--------

```gradle
repositories {
    mavenCentral()
}

dependencies {
    api "com.hendraanggrian.auto:bundles:$version"
    annotationProcessor "com.hendraanggrian.auto:bundles-compiler:$version" // or kapt
}
```

Extra binding
-------------

### Binding

`@BindExtra` for binding extra value to field, field cannot be private.
When key is not provided, field name will be used as the key.

```kotlin
@BindExtra @JvmField var username: String
```

### Wrapping

Create extras with varargs argument with `extrasOf()`.
This is optional, any Bundle would work just fine.

```java
Intent intent = new Intent(context, ExampleActivity.class);
intent.putExtras(Bundles.extrasOf(ExampleActivity.class, "Hendra Anggrian", 24));
startActivity(intent);
```

State binding
-------------

### Restoring

`@BindState` for binding extra value to field, field cannot be private.
When key is not provided, field name will be used as the key.

```kotlin
@BindExtra @JvmField var position: Int

override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    restoreStates(savedInstanceState)
}
```

### Saving

Simply call `Bundle.saveStates()` to save states.

```kotlin
override fun onSaveInstanceState(outState: Bundle) {
   super.onSaveInstanceState(outState)
   saveStates(outState)
}
```

Supported extra types
---------------------

- primitive data types and array of them.
- `CharSequence`, `CharSequence[]`, and `ArrayList<CharSequence>`
- `String`, `String[]`, and `ArrayList<String>`
- `Parcelable`, `Parcelable[]`, `ArrayList<Parcelable>` and `SparseArray<Parcelable>`
- `Serializable`

Parceler
--------

[Parceler][parceler] is supported with this library, it is a library that easily makes any object implements Parcelable with generated code, making it able to be inserted to Bundle as Parcelable.

`Bundles.wrap()` automatically converts the object to Parcelable.
Without `Bundles.wrap()`, object must be wrapped using `Parcels.wrap()`.
Head to [Parceler doc][parceler] for more information.

```java
User user = new User("Hendra Anggrian", 24);
// with Bundles.wrap()
intent.putExtras(Bundles.wrap(UserActivity.class, user));
// without Bundles.wrap()
intent.putExtra("user", Parcels.wrap(user));
```

`Bundles.bind()` automatically converts the Parcelable back to original object.

```kotlin
class UserActivity : Activity {
    @Extra("user") lateinit var user : User

    override fun onCreate(savedInstanceState : Bundle) {
        super.onCreate(savedInstanceState)
        bindExtras()
    }
}
```

Optional bindings
-----------------

Extra bindings are required by default, an exception will be thrown if key is not found in Bundle.
If this is not a desired behavior, annotate the field with `@Nullable` from [Android's support design library][support-annotations].

```java
@Nullable @BindExtra String username;
```