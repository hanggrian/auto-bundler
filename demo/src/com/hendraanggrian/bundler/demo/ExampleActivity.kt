package com.hendraanggrian.bundler.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.hendraanggrian.bundler.demo.model.User
import com.hendraanggrian.bundler.extrasOf
import kotlinx.android.synthetic.main.activity_example.*

class ExampleActivity : BaseActivity(), View.OnClickListener {

    override val contentLayout: Int
        get() = R.layout.activity_example

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar_main)
        button_main_example1.setOnClickListener(this)
        button_main_example2.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_main_example1 -> startActivity(
                Intent(this, Example1Activity::class.java)
                    .putExtras(extrasOf<Example1Activity>(true))
            )
            R.id.button_main_example2 -> {
                val user = User()
                user.name = "Hendra"
                startActivity(
                    Intent(this, Example2Activity::class.java)
                        .putExtras(extrasOf<Example2Activity>(user))
                )
            }
        }
    }
}