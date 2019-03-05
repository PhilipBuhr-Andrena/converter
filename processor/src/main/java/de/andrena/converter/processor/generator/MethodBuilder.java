package de.andrena.converter.processor.generator;

import com.squareup.javapoet.MethodSpec;
import de.andrena.converter.processor.informationextractor.ClassInformation;
import de.andrena.converter.processor.informationextractor.FieldInformation;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;

import java.util.Optional;

import static javax.tools.Diagnostic.Kind.NOTE;

class MethodBuilder {
    private Messager messager;
    private StatementBuilder statementBuilder;

    MethodBuilder() {
        this(new StatementBuilder());
    }

    MethodBuilder(StatementBuilder statementBuilder) {
        this.statementBuilder = statementBuilder;
    }

    MethodSpec generateConversionMethod(ClassInformation to, ClassInformation from) {
        String targetName = to.getSimpleName();
        log("writing conversion method to " + targetName);

        MethodSpec.Builder builder = MethodSpec.methodBuilder("create" + targetName);
        builder.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        builder.addParameter(from.getClassName(), "from");
        builder.addStatement("$T result = new $T()", to.getClassName(), to.getClassName());

        for (FieldInformation toField : to.getFields()) {
            Optional<FieldInformation> fromFieldOptional = from.findField(toField);
            fromFieldOptional.map(fromField -> statementBuilder.mapField(toField, fromField))
                    .ifPresent(builder::addStatement);

        }

        builder.addStatement("return result");
        builder.returns(to.getClassName());
        return builder.build();
    }

    private void log(String msg) {
        if (messager != null) {
            messager.printMessage(NOTE, msg);
        }
    }

    void setMessager(Messager messager) {
        this.messager = messager;
    }
}
