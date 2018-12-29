package com.hendraanggrian.bundler.demo

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.hendraanggrian.bundler.State
import com.hendraanggrian.bundler.extrasOf
import com.hendraanggrian.bundler.restoreStates
import com.hendraanggrian.bundler.saveStates
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity(override val contentLayout: Int = R.layout.activity_main) : BaseActivity(),
    View.OnClickListener {

    @State @JvmField var cardViewShown: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            restoreStates(savedInstanceState)
        }
        if (cardViewShown) {
            button.visibility = View.INVISIBLE
            cardView.visibility = View.VISIBLE
        }
        button.setOnClickListener(this)
        cardView.setOnClickListener(this)
        toolbar.setNavigationOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            button -> {
                val set =
                    createAnimatorSet(resources, revealFrameLayout.animateTo(button, cardView))
                set.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        button.visibility = View.INVISIBLE
                        cardView.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        cardViewShown = true
                    }
                })
                set.start()
            }
            toolbar -> {
                val set = createAnimatorSet(
                    resources,
                    revealFrameLayout.animateTo(button, cardView, true)
                )
                set.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        button.visibility = View.VISIBLE
                        cardView.visibility = View.INVISIBLE
                        cardViewShown = false
                    }
                })
                set.start()
            }
            cardView -> startActivity(
                Intent(this, NextActivity::class.java)
                    .putExtras(extrasOf<NextActivity>(createRect(revealFrameLayout, cardView)))
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
        }
    }

    override fun onBackPressed() {
        if (cardViewShown) onClick(toolbar)
        else super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // super.onSaveInstanceState(outState saveStatesTo this)
        super.onSaveInstanceState(outState)
        saveStates(outState)
    }

    companion object {

        fun createAnimatorSet(resources: Resources, animators: Collection<Animator>): AnimatorSet {
            val set = AnimatorSet()
            set.playTogether(animators)
            set.interpolator = FastOutSlowInInterpolator()
            set.duration = resources.getInteger(android.R.integer.config_longAnimTime).toLong()
            return set
        }

        fun createRect(parent: FrameLayout, child: View): Rect {
            val rect = Rect()
            child.getDrawingRect(rect)
            parent.offsetDescendantRectToMyCoords(child, rect)
            return rect
        }
    }
}