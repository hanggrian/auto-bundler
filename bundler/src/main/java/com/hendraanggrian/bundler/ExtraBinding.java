package com.hendraanggrian.bundler;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class ExtraBinding {

    @NonNull protected final Bundle source;
    @NonNull protected final Resources res;

    protected ExtraBinding(@NonNull Bundle source, @NonNull Resources res) {
        this.source = source;
        this.res = res;
    }
}