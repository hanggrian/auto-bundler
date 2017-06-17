package com.example.bundlerexample

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import butterknife.BindView
import com.hendraanggrian.bundler.BindState
import com.hendraanggrian.bundler.Bundler
import com.hendraanggrian.reveallayout.RevealableLayout

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class MainActivity(override val contentLayout: Int = R.layout.activity_main) : BaseActivity() {

    companion object {
        fun createAnimatorSet(resources: Resources, animators: Collection<Animator>): AnimatorSet {
            val set = AnimatorSet()
            set.playTogether(animators)
            set.interpolator = FastOutSlowInInterpolator()
            set.duration = resources.getInteger(android.R.integer.config_longAnimTime).toLong()
            return set
        }
    }

    @BindState @JvmField var cardViewShown: Boolean = false

    @BindView(R.id.revealableLayout) lateinit var revealableLayout: RevealableLayout
    @BindView(R.id.button) lateinit var button: Button
    @BindView(R.id.cardView) lateinit var cardView: CardView
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        button.setOnClickListener {
            val set = createAnimatorSet(resources, revealableLayout.animateTo(button, cardView))
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
        toolbar.setNavigationOnClickListener {
            val set = createAnimatorSet(resources, revealableLayout.animateTo(button, cardView, true))
            set.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    button.visibility = View.VISIBLE
                    cardView.visibility = View.INVISIBLE
                    cardViewShown = false
                }
            })
            set.start()
        }

        savedInstanceState?.let {
            Bundler.bindStates(this, it)
            if (cardViewShown) {
                button.visibility = View.INVISIBLE
                cardView.visibility = View.VISIBLE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Bundler.onSaveInstanceState(outState!!, javaClass, cardViewShown)
    }
}