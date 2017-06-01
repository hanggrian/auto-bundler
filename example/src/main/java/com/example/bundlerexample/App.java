package com.example.bundlerexample;

import android.app.Application;

import com.hendraanggrian.bundler.Bundler;

import butterknife.ButterKnife;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(BuildConfig.DEBUG);
        Bundler.setDebug(BuildConfig.DEBUG);
    }
}