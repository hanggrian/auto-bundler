package com.example.bundler

import android.app.Application
import com.hendraanggrian.bundler.Bundler

class ExampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Bundler.setDebug(true)
    }
}