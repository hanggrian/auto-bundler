package com.hendraanggrian.bundler.demo

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    @get:LayoutRes abstract val contentLayout: Int

    // private var unbinder: Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentLayout)
        // unbinder = ButterKnife.bind(this)
    }

    /* override fun onDestroy() {
        super.onDestroy()
        unbinder!!.unbind()
    } */
}