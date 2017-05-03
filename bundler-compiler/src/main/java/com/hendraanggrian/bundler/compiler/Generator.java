package com.hendraanggrian.bundler.compiler;

import android.support.annotation.NonNull;

import java.io.IOException;

import javax.annotation.processing.Filer;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
interface Generator {

    void generate(@NonNull Filer filer) throws IOException;
}