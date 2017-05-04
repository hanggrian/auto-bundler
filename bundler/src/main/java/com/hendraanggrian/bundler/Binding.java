package com.hendraanggrian.bundler;

import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
abstract class Binding {

    @NonNull protected final Bundle source;

    Binding(@NonNull Bundle source) {
        this.source = source;
    }
}