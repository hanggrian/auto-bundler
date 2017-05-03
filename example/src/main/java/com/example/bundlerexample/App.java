package com.example.bundlerexample;

import android.app.Application;

import com.hendraanggrian.bundler.Bundler;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bundler.setDebug(true);
    }
}