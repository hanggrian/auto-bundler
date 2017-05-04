package com.hendraanggrian.bundler;

import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public abstract class ExtraBinding {

    @NonNull protected final Bundle source;

    protected ExtraBinding(@NonNull Bundle source) {
        this.source = source;
    }
}