package de.andrena.converter.processor.generator;

import com.google.common.annotations.VisibleForTesting;
import com.squareup.javapoet.TypeSpec;
import de.andrena.converter.processor.informationextractor.ClassInformation;
import de.andrena.converter.processor.informationextractor.ConversionInformation;
import de.andrena.converter.processor.informationextractor.ConversionMethods;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import java.io.IOException;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

public class ConverterBuilder {
    private Filer filer;
    private Messager messager;
    private MethodBuilder methodBuilder;
    private JavaFileWrapper javaFileWrapper;

    public ConverterBuilder() {
        this(new MethodBuilder(), new JavaFileWrapper());
    }

    @VisibleForTesting
    ConverterBuilder(MethodBuilder methodBuilder, JavaFileWrapper javaFileWrapper) {
        this.methodBuilder = methodBuilder;
        this.javaFileWrapper = javaFileWrapper;
    }

    public void generate(ConversionInformation conversionInformation, ConversionMethods conversionMethods) {
        log("Building Converter for" + conversionInformation.getName(), NOTE);
        TypeSpec.Builder builder = TypeSpec.classBuilder(conversionInformation.getName() + "Converter")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        addMethods(conversionInformation, conversionMethods, builder);
        try {
            javaFileWrapper.createJavaFile(conversionInformation.getPackageName(), builder, filer);
        } catch (IOException e) {
            log("Failed to write Converter for Class: " + conversionInformation.getModel().getSimpleName(), ERROR);
            throw new FileWritingException("Failed to write Converter for Class: " + conversionInformation.getModel().getSimpleName(), e);
        }
    }

    private void addMethods(ConversionInformation conversionInformation, ConversionMethods conversionMethods, TypeSpec.Builder builder) {
        ClassInformation model = conversionInformation.getModel();
        for (ClassInformation source : conversionInformation.getSources()) {
            builder.addMethod(methodBuilder.generateConversionMethod(model, source, conversionMethods));
            builder.addMethod(methodBuilder.generateConversionMethod(source, model, conversionMethods));
        }
    }

    private void log(String msg, Diagnostic.Kind level) {
        if (messager != null) {
            messager.printMessage(level, msg);
        }
    }

    public void setFiler(Filer filer) {
        this.filer = filer;
    }

    public void setMessager(Messager messager) {
        this.messager = messager;
        if (methodBuilder != null) {
            methodBuilder.setMessager(messager);
        }
    }
}
