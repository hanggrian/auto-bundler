package com.hendraanggrian.bundler;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public abstract class ExtrasBinding extends Binding {

    @NonNull private final List args;

    protected ExtrasBinding(@NonNull List args) {
        super(new Bundle());
        this.args = args;
    }

    @Nullable
    protected Object next() {
        Object arg = args.get(0);
        args.remove(0);
        return arg;
    }
}