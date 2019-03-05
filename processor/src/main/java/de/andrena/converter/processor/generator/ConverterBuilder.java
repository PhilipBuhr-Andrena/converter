package de.andrena.converter.processor.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import de.andrena.converter.processor.informationextractor.ClassInformation;
import de.andrena.converter.processor.informationextractor.ConversionInformation;

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

    public ConverterBuilder() {
        this(new MethodBuilder());
    }

    public ConverterBuilder(MethodBuilder methodBuilder) {
        this.methodBuilder = methodBuilder;
    }

    public void generate(ConversionInformation conversionInformation) {
        log("Building Converter for" + conversionInformation.getName(), NOTE);
        TypeSpec.Builder builder = TypeSpec.classBuilder(conversionInformation.getName() + "Converter")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        ClassInformation model = conversionInformation.getModel();
        for (ClassInformation source : conversionInformation.getSources()) {
            builder.addMethod(methodBuilder.generateConversionMethod(model, source));
            builder.addMethod(methodBuilder.generateConversionMethod(source, model));
        }
        JavaFile javaFile = JavaFile.builder(conversionInformation.getPackageName(), builder.build()).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            log("Failed to write Converter: " + conversionInformation.getName(), ERROR);
        } catch (NullPointerException npe) {
            log("Failed to write Converter: " + conversionInformation.getName(), ERROR);

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
