package de.andrena.converter.processor.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import java.io.IOException;

class JavaFileWrapper {
    void createJavaFile(String packageName, TypeSpec.Builder builder, Filer filer) throws IOException {
        JavaFile javaFile = JavaFile.builder(packageName, builder.build()).build();

        javaFile.writeTo(filer);
    }
}
