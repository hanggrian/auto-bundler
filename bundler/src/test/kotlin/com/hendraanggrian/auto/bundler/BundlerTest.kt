package com.hendraanggrian.auto.bundler

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.common.truth.Truth.assertThat
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class BundlerTest {
    private lateinit var context: Context

    @BeforeTest
    fun setupContext() {
        context = Robolectric.buildActivity(TestActivity::class.java).setup().get()
    }

    @BeforeTest
    @AfterTest
    fun clearCache() {
        Bundler.BINDINGS?.clear()
    }

    @Test
    fun `Bind empty`() {
        val bundle = Bundle()
        val view = View(context)
        assertThat(Bundler.BINDINGS).isNull()
        Bundler.bindExtras(bundle, view)
        assertThat(Bundler.BINDINGS).isNotNull()
        assertThat(Bundler.BINDINGS).isEmpty()
    }
}
