package com.example.bundlerexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hendraanggrian.bundler.Bundler;
import com.hendraanggrian.bundler.annotations.BindExtra;

public class Example1Activity extends BaseActivity {

    @BindExtra("hello") String hello;
    @BindExtra("world") String world;
    @BindExtra("asd") @Nullable String asd;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_example1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundler.bind(this);
        Log.d("hello", hello);
        Log.d("world", world);
    }
}