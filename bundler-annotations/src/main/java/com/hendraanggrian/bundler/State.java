package com.hendraanggrian.bundler;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Target(FIELD)
@Retention(CLASS)
public @interface State {

    String SUFFIX = "_StateBinding";

    /**
     * Key in bundle key-value pair.
     * Leave it empty to use field name as the key.
     */
    String value() default "";
}