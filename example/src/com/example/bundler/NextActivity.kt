package com.example.bundler

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import butterknife.BindView
import com.hendraanggrian.bundler.Bundler
import com.hendraanggrian.bundler.Extra
import com.hendraanggrian.widget.RevealFrameLayout


/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class NextActivity(override val contentLayout: Int = R.layout.activity_next) : BaseActivity() {

    @Extra lateinit var rect: Rect

    @BindView(R.id.revealFrameLayout) lateinit var revealFrameLayout: RevealFrameLayout
    @BindView(R.id.view) lateinit var view: View
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        Bundler.bindExtras(this)
        view.post { revealFrameLayout.animate(view, rect.centerX(), rect.centerY()).start() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val animator = revealFrameLayout.animate(view, rect.centerX(), rect.centerY(), true)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.INVISIBLE
                finish()
                overridePendingTransition(0, 0)
            }
        })
        animator.start()
    }
}