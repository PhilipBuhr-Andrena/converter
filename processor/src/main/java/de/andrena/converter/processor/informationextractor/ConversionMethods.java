package de.andrena.converter.processor.informationextractor;

import com.google.common.annotations.VisibleForTesting;

import javax.lang.model.type.TypeMirror;
import java.util.List;

public class ConversionMethods {
    private List<ConversionMethod> methods;

    public ConversionMethods(List<ConversionMethod> methods) {
        this.methods = methods;
    }

    public String findAdapter(TypeMirror toType, TypeMirror fromType) {
        return methods.stream()
                .filter(method -> method.hasCorrectSignature(toType, fromType))
                .findFirst()
                .map(ConversionMethod::constructFullName)
                .orElseThrow(() -> new ConversionAdapterNotFoundException("ConversionAdapter not found for types: " + toType.toString() + " and " + fromType.toString()));

    }

    @VisibleForTesting
    List<ConversionMethod> getMethods() {
        return methods;
    }
}
