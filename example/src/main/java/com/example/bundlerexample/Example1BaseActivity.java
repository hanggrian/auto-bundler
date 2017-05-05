package com.example.bundlerexample;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.hendraanggrian.bundler.Bundler;
import com.hendraanggrian.bundler.annotations.BindExtra;
import com.hendraanggrian.bundler.annotations.WrapExtras;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@WrapExtras
public abstract class Example1BaseActivity extends BaseActivity {

    @Nullable @BindExtra("BOOLEAN") boolean BOOLEAN;
    @Nullable @BindExtra("BOOLEAN_BOXED") Boolean BOOLEAN_BOXED;
    @Nullable @BindExtra("BOOLEAN_ARRAY") boolean[] BOOLEAN_ARRAY;
    @Nullable @BindExtra("BYTE") byte BYTE;
    @Nullable @BindExtra("BYTE_BOXED") Byte BYTE_BOXED;
    @Nullable @BindExtra("BYTE_ARRAY") byte[] BYTE_ARRAY;
    @Nullable @BindExtra("CHAR") char CHAR;
    @Nullable @BindExtra("CHAR_BOXED") Character CHAR_BOXED;
    @Nullable @BindExtra("CHAR_ARRAY") char[] CHAR_ARRAY;
    @Nullable @BindExtra("CHARSEQUENCE") CharSequence CHARSEQUENCE;
    @Nullable @BindExtra("CHARSEQUENCE_ARRAY") CharSequence[] CHARSEQUENCE_ARRAY;
    @Nullable @BindExtra("CHARSEQUENCE_ARRAYLIST") ArrayList<CharSequence> CHARSEQUENCE_ARRAYLIST;
    @Nullable @BindExtra("DOUBLE") double DOUBLE;
    @Nullable @BindExtra("DOUBLE_BOXED") Double DOUBLE_BOXED;
    @Nullable @BindExtra("DOUBLE_ARRAY") double[] DOUBLE_ARRAY;
    @Nullable @BindExtra("FLOAT") float FLOAT;
    @Nullable @BindExtra("FLOAT_BOXED") Float FLOAT_BOXED;
    @Nullable @BindExtra("FLOAT_ARRAY") float[] FLOAT_ARRAY;
    @Nullable @BindExtra("LONG") long LONG;
    @Nullable @BindExtra("LONG_BOXED") Long LONG_BOXED;
    @Nullable @BindExtra("LONG_ARRAY") long[] LONG_ARRAY;
    @Nullable @BindExtra("INT") int INT;
    @Nullable @BindExtra("INT_BOXED") Integer INT_BOXED;
    @Nullable @BindExtra("INT_ARRAY") int[] INT_ARRAY;
    @Nullable @BindExtra("INT_ARRAYLIST") ArrayList<Integer> INT_ARRAYLIST;
    @Nullable @BindExtra("PARCELABLE") Parcelable PARCELABLE;
    @Nullable @BindExtra("PARCELABLE_ARRAY") Parcelable[] PARCELABLE_ARRAY;
    @Nullable @BindExtra("PARCELABLE_ARRAYLIST") ArrayList<Parcelable> PARCELABLE_ARRAYLIST;
    @Nullable @BindExtra("PARCELABLE_SPARSEARRAY") SparseArray<Parcelable> PARCELABLE_SPARSEARRAY;
    @Nullable @BindExtra("SERIALIZABLE") Serializable SERIALIZABLE;
    @Nullable @BindExtra("SHORT") short SHORT;
    @Nullable @BindExtra("SHORT_BOXED") Short SHORT_BOXED;
    @Nullable @BindExtra("SHORT_ARRAY") short[] SHORT_ARRAY;
    @Nullable @BindExtra("STRING") String STRING;
    @Nullable @BindExtra("STRING_ARRAY") String[] STRING_ARRAY;
    @Nullable @BindExtra("STRING_ARRAYLIST") ArrayList<String> STRING_ARRAYLIST;
    @Nullable @BindExtra List<String> asd;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_example1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundler.bind(this);
    }
}