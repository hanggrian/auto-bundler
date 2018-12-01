package com.hendraanggrian.bundler.demo

import android.os.Bundle
import android.util.Log
import com.hendraanggrian.bundler.Extra
import com.hendraanggrian.bundler.bindExtras
import com.hendraanggrian.bundler.demo.model.User

class Example2Activity(override val contentLayout: Int = R.layout.activity_example1) :
    BaseActivity() {

    @Extra("user") lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindExtras()
        Log.d("user.name", user.name)
    }
}