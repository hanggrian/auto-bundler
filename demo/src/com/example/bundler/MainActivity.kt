package com.example.bundler

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import butterknife.BindView
import com.hendraanggrian.bundler.Bundler
import com.hendraanggrian.bundler.State
import com.hendraanggrian.widget.RevealFrameLayout

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class MainActivity(override val contentLayout: Int = R.layout.activity_main) : BaseActivity(), View.OnClickListener {

    @State @JvmField var cardViewShown: Boolean = false

    @BindView(R.id.revealFrameLayout) lateinit var revealFrameLayout: RevealFrameLayout
    @BindView(R.id.button) lateinit var button: Button
    @BindView(R.id.cardView) lateinit var cardView: CardView
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) Bundler.restoreStates(this, savedInstanceState)
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
                val set = createAnimatorSet(resources, revealFrameLayout.animateTo(button, cardView))
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
                val set = createAnimatorSet(resources,
                    revealFrameLayout.animateTo(button, cardView, true))
                set.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        button.visibility = View.VISIBLE
                        cardView.visibility = View.INVISIBLE
                        cardViewShown = false
                    }
                })
                set.start()
            }
            cardView -> startActivity(Intent(this, NextActivity::class.java)
                .putExtras(Bundler.extrasOf(NextActivity::class.java,
                    createRect(revealFrameLayout, cardView)))
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
    }

    override fun onBackPressed() {
        if (cardViewShown) onClick(toolbar)
        else super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // super.onSaveInstanceState(outState saveStatesTo this)
        super.onSaveInstanceState(outState)
        Bundler.saveStates(this, outState)
    }

    companion object {
        fun createAnimatorSet(resources: Resources, animators: Collection<Animator>): AnimatorSet {
            val set = AnimatorSet()
            set.playTogether(animators)
            set.interpolator = FastOutSlowInInterpolator()
            set.duration = resources.getInteger(android.R.integer.config_longAnimTime).toLong()
            return set
        }

        fun createRect(parent: ViewGroup, child: View): Rect {
            val rect = Rect()
            child.getDrawingRect(rect)
            parent.offsetDescendantRectToMyCoords(child, rect)
            return rect
        }
    }
}