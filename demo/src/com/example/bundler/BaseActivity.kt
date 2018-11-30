package com.example.bundler

import android.os.Bundle
import android.support.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
abstract class BaseActivity : AppCompatActivity() {

    @get:LayoutRes abstract val contentLayout: Int

    private var unbinder: Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentLayout)
        unbinder = ButterKnife.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder!!.unbind()
    }
}