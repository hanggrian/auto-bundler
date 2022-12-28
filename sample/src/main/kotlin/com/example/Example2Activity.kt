package com.example

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.hendraanggrian.auto.bundler.BindState
import com.hendraanggrian.auto.bundler.restoreStates
import com.hendraanggrian.auto.bundler.saveStates
import kotlinx.android.synthetic.main.activity_example1.toolbar
import kotlinx.android.synthetic.main.activity_example2.*

class Example2Activity : AppCompatActivity() {
    @JvmField @BindState var string = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example2)
        setSupportActionBar(toolbar)
        saveButton.setOnClickListener { string = editText.text.toString() }
        showButton.setOnClickListener { Toast.makeText(this, string, LENGTH_SHORT).show() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveStates(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        restoreStates(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
