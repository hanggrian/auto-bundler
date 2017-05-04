Bundler
=======
Android extra field binder with annotation processor. 
 * Uses [Parceler](https://github.com/johncarl81/parceler) to automatically unwrap objects.

```java
public class ProfileActivity extends Activity {
    @BindExtra("username") String username;
    @BindExtra("age") int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        Bundler.bind(this);
        // TODO Use fields...
    }
}
```

Download
--------
```gradle
dependencies {
    compile 'com.hendraanggrian:bundler:0.1.0'
    annotationProcessor 'com.hendraanggrian:bundler-compiler:0.1.0'
}
```