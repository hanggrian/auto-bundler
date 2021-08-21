package com.example.bundles

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.hendraanggrian.auto.bundles.BindExtra
import com.hendraanggrian.auto.bundles.bindExtras
import kotlinx.android.synthetic.main.activity_example1.*

class Example1Activity : AppCompatActivity() {
    @BindExtra @JvmField var title: String = ""
    @BindExtra @JvmField @Nullable var subtitle: String? = null

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}