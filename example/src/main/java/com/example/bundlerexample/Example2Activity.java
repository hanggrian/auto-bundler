package com.example.bundlerexample;

import android.os.Bundle;
import android.util.Log;

import com.example.bundlerexample.model.User;
import com.hendraanggrian.bundler.Bundler;
import com.hendraanggrian.bundler.annotations.BindExtra;
import com.hendraanggrian.bundler.annotations.WrapExtras;

@WrapExtras
public class Example2Activity extends BaseActivity {

    @BindExtra("user") User user;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_example1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundler.bind(this);
        Log.d("user.name", user.name);
    }
}