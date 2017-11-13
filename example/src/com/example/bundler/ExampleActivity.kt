package com.example.bundler

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import butterknife.BindView
import com.example.bundler.model.User
import com.hendraanggrian.bundler.wrapExtras

class ExampleActivity : BaseActivity(), View.OnClickListener {

    @BindView(R.id.toolbar_main) lateinit var toolbar: Toolbar
    @BindView(R.id.button_main_example1) lateinit var buttonExample1: Button
    @BindView(R.id.button_main_example2) lateinit var buttonExample2: Button

    override val contentLayout: Int
        get() = R.layout.activity_example

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        buttonExample1.setOnClickListener(this)
        buttonExample2.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_main_example1 -> startActivity(Intent(this, Example1Activity::class.java)
                    .putExtras(wrapExtras(Example1Activity::class.java, true)))
            R.id.button_main_example2 -> {
                val user = User()
                user.name = "Hendra"
                startActivity(Intent(this, Example2Activity::class.java)
                        .putExtras(wrapExtras(Example2Activity::class.java, user)))
            }
        }
    }
}