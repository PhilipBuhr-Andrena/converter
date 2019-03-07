package de.andrena.converter.processor.informationextractor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

class ConversionMethod {
    private ExecutableElement methodElement;

    ConversionMethod(ExecutableElement methodElement) {
        this.methodElement = methodElement;
    }

    String constructFullName() {
        String qualifiedClassName = methodElement.getEnclosingElement().asType().toString();
        String methodName = methodElement.getSimpleName().toString();
        return qualifiedClassName + "." + methodName;
    }

    boolean hasCorrectSignature(TypeMirror toType, TypeMirror fromType) {
        TypeMirror returnType = methodElement.getReturnType();
        TypeMirror parameterType = methodElement.getParameters().get(0).asType();
        return toType.equals(returnType) && fromType.equals(parameterType);
    }
}
