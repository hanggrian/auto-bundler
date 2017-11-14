package com.example.bundler

import android.os.Bundle
import com.example.bundler.model.User
import com.hendraanggrian.bundler.Extra
import com.hendraanggrian.bundler.bindExtras
import kota.debug

class Example2Activity(override val contentLayout: Int = R.layout.activity_example1) : BaseActivity() {

    @Extra("user") lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindExtras()
        debug("user.name", user.name)
    }
}