package com.example

import androidx.multidex.MultiDexApplication
import com.hendraanggrian.auto.bundler.Bundler

class ExampleApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Bundler.setDebug(true)
    }
}
