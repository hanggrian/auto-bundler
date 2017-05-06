package com.example.bundlerexample.test;

import com.hendraanggrian.bundler.annotations.BindExtra;

/**
 * Created by hendraanggrian on 5/3/17.
 */
public class B extends A {
    @BindExtra("b") String b;
}
