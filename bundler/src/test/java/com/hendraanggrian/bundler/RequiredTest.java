package com.hendraanggrian.bundler;

import com.google.testing.compile.JavaFileObjects;
import com.hendraanggrian.bundler.compiler.BundlerProcessor;

import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class RequiredTest {

    @Test
    public void simple() throws Exception {
        assertAbout(javaSource())
                .that(JavaFileObjects.forSourceLines("test.Test",
                        "package test;",
                        "import android.support.annotation.Nullable;",
                        "import com.hendraanggrian.bundler.BindExtra;",
                        "public class Test {",
                        "   @BindExtra boolean required;",
                        "   @Nullable @BindExtra Boolean notRequired;",
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
                        "import com.hendraanggrian.bundler.BundleBinding;",
                        "import java.util.List;",
                        "public class Test_ExtraBinding extends BundleBinding {",
                        "   public Test_ExtraBinding(Test target, Bundle source) {",
                        "       super(source);",
                        "       checkRequired(\"required\", \"required\");",
                        "       target.required = getBoolean(\"required\", target.required);",
                        "       target.notRequired = getBoolean(\"notRequired\", target.notRequired);",
                        "   }",
                        "   public Test_ExtraBinding(List args) {",
                        "       super(args);",
                        "       if(!args.isEmpty()) source.putBoolean(\"required\", (boolean) nextArg());",
                        "       if(!args.isEmpty()) source.putBoolean(\"notRequired\", (boolean) nextArg());",
                        "   }",
                        "}"
                ));
    }
}