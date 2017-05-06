package com.hendraanggrian.bundler;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
final class ShrinkingArrayList<E> extends ArrayList<E> {

    ShrinkingArrayList(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public E get(int index) {
        E object = super.get(index);
        remove(index);
        return object;
    }
}