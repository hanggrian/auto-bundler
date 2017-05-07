package com.hendraanggrian.bundler;

/**
 * Mini guava's Preconditions.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
final class Preconditions {

    static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}