package com.example.bundles

import androidx.multidex.MultiDexApplication
import com.hendraanggrian.auto.bundles.Bundles

class ExampleApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Bundles.setDebug(true)
    }
}