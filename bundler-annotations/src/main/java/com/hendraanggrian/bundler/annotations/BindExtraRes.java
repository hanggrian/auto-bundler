package com.hendraanggrian.bundler.annotations;

import android.support.annotation.StringRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface BindExtraRes {

    @StringRes int value();
}