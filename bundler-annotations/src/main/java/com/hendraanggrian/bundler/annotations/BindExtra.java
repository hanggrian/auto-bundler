package com.hendraanggrian.bundler.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bind extra value to field with this annotation.
 * Field cannot be private as it would be inaccessible to binding class.
 * <pre><code>
 * {@literal @}BindExtra("EXTRA_COUNTRY") String country;
 * </code></pre>
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface BindExtra {

    String SUFFIX = "_BundleBinding";

    /**
     * Key in bundle key-value pair.
     * Leave it empty to use field name as the key.
     */
    String value() default "";
}