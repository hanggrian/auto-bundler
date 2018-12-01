package com.hendraanggrian.bundler.demo

import android.app.Application
import com.hendraanggrian.bundler.Bundler

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Bundler.setDebug(BuildConfig.DEBUG)
    }
}