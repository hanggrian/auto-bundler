Bundler
=======
Passing key-value pairs with Bundle is the most common approach of exchanging data across Android components.
Unfortunately, much of the process of sending and receiving those key-value pairs (known as extra) requires a lot of boilerplate code.
Bundler aims to minify the process with annotation processing.

```java
public class ExampleActivity extends Activity {
    @BindExtra String username;
    @BindState int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundler.initExtras(this);
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

Extra binding
-------------
#### Binding
`@BindExtra` for binding extra value to field, field cannot be private.
When key is not provided, field name will be used as the key.
```java
@BindExtra String username;
```

#### Wrapping
Create extras with varargs argument with `Bundle.wrapExtras()`.
This is optional, any Bundle would work just fine.
```java
Intent intent = new Intent(context, ExampleActivity.class);
intent.putExtras(Bundler.wrapExtras(ExampleActivity.class, "Hendra Anggrian", 24));
startActivity(intent);
```

State binding
-------------
#### Restoring
`@BindState` for binding extra value to field, field cannot be private.
When key is not provided, field name will be used as the key.
```java
@BindState int position;
```

#### Saving
Simply call `Bundle.saveStates()` to save states.
```java
@Override
public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Bundler.saveStates(this, outState);
}
```

Supported extra types
---------------------
 * primitive data types and array of them.
 * `CharSequence`, `CharSequence[]`, and `ArrayList<CharSequence>`
 * `String`, `String[]`, and `ArrayList<String>`
 * `Parcelable`, `Parcelable[]`, `ArrayList<Parcelable>`,
   and `SparseArray<Parcelable>`
 * `Serializable`
 
Parceler
--------
[Parceler][parceler] is supported with this library, it is a library that easily makes any object implements Parcelable with generated code, making it able to be inserted to Bundle as Parcelable.

`Bundler.wrap()` automatically converts the object to Parcelable.
Without `Bundler.wrap()`, object must be wrapped using `Parcels.wrap()`.
Head to [Parceler doc][parceler] for more information.
```java
User user = new User("Hendra Anggrian", 24);
// with Bundler.wrap()
intent.putExtras(Bundler.wrap(UserActivity.class, user));
// without Bundler.wrap()
intent.putExtra("user", Parcels.wrap(user));
```

`Bundler.bind()` automatically converts the Parcelable back to original object.
```java
public class UserActivity extends Activity {
    @BindExtra("user") User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundler.bind(this);
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

Download
--------
```gradle
repositories {
    jcenter()
    maven { url "https://maven.google.com" }
}

dependencies {
    compile 'com.hendraanggrian:bundler:0.5.0'
    annotationProcessor 'com.hendraanggrian:bundler-compiler:0.5.0'
}
```

License
-------
    Copyright 2017 Hendra Anggrian

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[parceler]: https://github.com/johncarl81/parceler
[support-annotations]: http://tools.android.com/tech-docs/support-annotations
