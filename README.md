Bundler
=======
Passing key-value pairs with Bundle is the most common approach of exchanging
data across Android components. Unfortunately, much of the process of sending
and receiving those key-value pairs (known as extra) requires a lot of
boilerplate code. Bundler aims to minify the process with annotation processing.

`@BindExtra` for binding extra value to field, when no key is not provided,
field name will be used as the key.
```java
@WrapExtras
public class ExampleActivity extends Activity {
    @BindExtra String username;
    @BindExtra int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundler.bind(this);
        // TODO: Use fields...
    }
}
```

`@WrapExtras` for creating Bundle with with single `Bundler.wrap()` call.
This is optional, any other Bundle or adding extra to Intent by `putExtra()`
would work just fine.
```java
Intent intent = new Intent(context, ExampleActivity.class);
intent.putExtras(Bundler.wrap(ExampleActivity.class, "Hendra Anggrian", 24));
startActivity(intent);
```

Supported extra types
---------------------
By default, Android Bundle supports:
 * primitive data types and array of them.
 * `CharSequence`, `CharSequence[]`, and `ArrayList<CharSequence>`
 * `String`, `String[]`, and `ArrayList<String>`
 * `Parcelable`, `Parcelable[]`, `ArrayList<Parcelable>`,
   and `SparseArray<Parcelable>`
 * `Serializable`
 * object of any class annotated with `@Parcel`

[Parceler][1]

Download
--------
```groovy
dependencies {
    compile 'com.hendraanggrian:bundler:0.1.0'
    annotationProcessor 'com.hendraanggrian:bundler-compiler:0.1.0'
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


 [1]: https://github.com/johncarl81/parceler