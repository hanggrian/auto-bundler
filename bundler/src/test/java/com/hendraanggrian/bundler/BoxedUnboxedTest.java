package com.hendraanggrian.bundler;

import com.google.testing.compile.JavaFileObjects;
import com.hendraanggrian.bundler.compiler.BundlerProcessor;

import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class BoxedUnboxedTest {

    @Test
    public void simple() throws Exception {
        assertAbout(javaSource())
                .that(JavaFileObjects.forSourceLines("test.Test",
                        "package test;",
                        "import com.hendraanggrian.bundler.annotations.BindExtra;",
                        "public class Test {",
                        "   @BindExtra Boolean boxed;",
                        "   @BindExtra boolean unboxed;",
                        "}"
                ))
                .withCompilerOptions("-Xlint:-processing")
                .processedWith(new BundlerProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forSourceLines("test/Test_ExtraBinding",
                        "// Bundler generated class, do not modify! https://github.com/HendraAnggrian/bundler",
                        "package test;",
                        "import android.os.Bundle;",
                        "import com.hendraanggrian.bundler.ExtraBinding;",
                        "import java.util.List;",
                        "public class Test_ExtraBinding extends ExtraBinding {",
                        "   public Test_ExtraBinding(Test target, Bundle source) {",
                        "       super(source);",
                        "       checkRequired(\"boxed\", \"Test#boxed\");",
                        "       target.boxed = getBoolean(\"boxed\", target.boxed);",
                        "       checkRequired(\"unboxed\", \"Test#unboxed\");",
                        "       target.unboxed = getBoolean(\"unboxed\", target.unboxed);",
                        "   }",
                        "   public Test_ExtraBinding(List args) {",
                        "       super(args);",
                        "       if(!args.isEmpty()) source.putBoolean(\"boxed\", (java.lang.Boolean) args.get(0));",
                        "       if(!args.isEmpty()) source.putBoolean(\"unboxed\", (boolean) args.get(0));",
                        "   }",
                        "}"
                ));
    }
}