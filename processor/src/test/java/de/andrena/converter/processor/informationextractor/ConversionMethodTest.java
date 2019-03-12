/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.processor.informationextractor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConversionMethodTest {

    private TypeMirror toType;
    private VariableElement parameterType;
    private ExecutableElement methodElement;
    private TypeMirror fromType;

    @BeforeEach
    void setUp() {
        toType = mock(TypeMirror.class);
        fromType = mock(TypeMirror.class);

        parameterType = mock(VariableElement.class);
        when(parameterType.asType()).thenReturn(fromType);

        methodElement = mock(ExecutableElement.class);
        when(methodElement.getReturnType()).thenReturn(toType);
        when(methodElement.getParameters()).thenAnswer(invocation -> Collections.singletonList(parameterType));
    }

    @Test
    void returnsTrueIfItHasMatchingSignature() {
        ConversionMethod conversionMethod = new ConversionMethod(methodElement);
        assertThat(conversionMethod.hasCorrectSignature(toType, fromType)).isTrue();
    }

    @Test
    void returnsFalseIfSignatureDoesntMatch() {
        TypeMirror differentParameterType = mock(TypeMirror.class);
        when(parameterType.asType()).thenReturn(differentParameterType);

        ConversionMethod conversionMethod = new ConversionMethod(methodElement);
        assertThat(conversionMethod.hasCorrectSignature(toType, fromType)).isFalse();
    }

    @Test
    void handlesGenerics() {
        when(toType.toString()).thenReturn("ToType<TypeParameter>");
        when(fromType.toString()).thenReturn("FromType<TypeParameter>");

        ConversionMethod conversionMethod = new ConversionMethod(methodElement);
        assertThat(conversionMethod.hasCorrectSignature(toType, fromType)).isTrue();


    }

    @Test
    void returnsFullMethodName() {
        Name name = mock(Name.class);
        when(name.toString()).thenReturn("testMethod");
        when(methodElement.getSimpleName()).thenReturn(name);

        TypeMirror classType = mock(TypeMirror.class);
        when(classType.toString()).thenReturn("com.example.TestClass");

        TypeElement testClass = mock(TypeElement.class);
        when(testClass.asType()).thenReturn(classType);
        when(methodElement.getEnclosingElement()).thenReturn(testClass);

        ConversionMethod conversionMethod = new ConversionMethod(methodElement);

        assertThat(conversionMethod.constructFullName()).isEqualTo("com.example.TestClass.testMethod");
    }
}