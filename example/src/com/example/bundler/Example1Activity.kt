package com.example.bundler

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.hendraanggrian.bundler.BindExtra
import com.hendraanggrian.bundler.bindExtras
import kotlinx.android.synthetic.main.activity_example1.*

class Example1Activity : AppCompatActivity() {
    @BindExtra lateinit var title: String
    @BindExtra @Nullable @JvmField var subtitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example1)
        setSupportActionBar(toolbar)
        bindExtras()
        titleView.text = title
        if (subtitle != null) {
            subtitleView.isVisible = true
            subtitleView.text = subtitle
        }
    }
}