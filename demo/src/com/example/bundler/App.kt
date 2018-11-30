package com.example.bundler

import android.app.Application
import butterknife.ButterKnife
import com.hendraanggrian.bundler.Bundler

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ButterKnife.setDebug(BuildConfig.DEBUG)
        Bundler.setDebug(BuildConfig.DEBUG)
    }
}