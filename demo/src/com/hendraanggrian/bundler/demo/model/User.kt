package com.hendraanggrian.bundler.demo.model

import org.parceler.Parcel

@Parcel
class User {
    @JvmField var name: String? = null
    @JvmField var age: Int = 0
}