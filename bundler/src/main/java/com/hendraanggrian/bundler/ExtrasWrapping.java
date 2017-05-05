package com.hendraanggrian.bundler;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public abstract class ExtrasWrapping {

    static final ExtrasWrapping EMPTY;

    static {
        EMPTY = new ExtrasWrapping(Collections.EMPTY_LIST) {
        };
    }

    @NonNull protected final Bundle source;
    @NonNull private final List args;

    protected ExtrasWrapping(@NonNull List args) {
        this.source = new Bundle();
        this.args = args;
    }

    @Nullable
    protected Object next() {
        Object arg = args.get(0);
        args.remove(0);
        return arg;
    }
}