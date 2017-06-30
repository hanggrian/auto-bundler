package com.example.bundlerexample

import android.os.Bundle
import android.util.Log
import com.example.bundlerexample.model.User
import com.hendraanggrian.bundler.BindExtra
import com.hendraanggrian.bundler.Bundler

class Example2Activity(override val contentLayout: Int = R.layout.activity_example1) : BaseActivity() {

    @BindExtra("user") lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Bundler.bindExtras(this)
        Log.d("user.name", user.name)
    }
}