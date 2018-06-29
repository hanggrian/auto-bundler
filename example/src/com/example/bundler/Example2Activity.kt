package com.example.bundler

import android.os.Bundle
import android.util.Log
import com.example.bundler.model.User
import com.hendraanggrian.bundler.Bundler
import com.hendraanggrian.bundler.Extra

class Example2Activity(override val contentLayout: Int = R.layout.activity_example1) : BaseActivity() {

    @Extra("user") lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Bundler.bindExtras(this)
        Log.d("user.name", user.name)
    }
}