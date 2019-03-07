/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.processor.informationextractor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConversionMethodsTest {

    private static final String METHOD_NAME = "Testclass.testmethod";
    private TypeMirror toType;
    private TypeMirror fromType;
    private ConversionMethods conversionMethods;

    @BeforeEach
    void setUp() {
        toType = mock(TypeMirror.class);
        fromType = mock(TypeMirror.class);
        ConversionMethod conversionMethod = mock(ConversionMethod.class);
        when(conversionMethod.constructFullName()).thenReturn(METHOD_NAME);
        when(conversionMethod.hasCorrectSignature(toType, fromType)).thenReturn(true);
        conversionMethods = new ConversionMethods(Collections.singletonList(conversionMethod));
    }

    @Test
    void findCorrectMethodBySignature() {


        String adapter = conversionMethods.findAdapter(toType, fromType);
        assertThat(adapter).isEqualTo(METHOD_NAME);
    }

    @Test
    void throwsConversionAdapterNotFoundExceptionIfUnknownConversionRequired() {
        TypeMirror type1 = mock(TypeMirror.class);
        TypeMirror type2 = mock(TypeMirror.class);

        when(type1.toString()).thenReturn("TestType1");
        when(type2.toString()).thenReturn("TestType2");

        assertThatThrownBy(() -> conversionMethods.findAdapter(type1, type2))
                .isInstanceOf(ConversionAdapterNotFoundException.class).hasMessage("Conversion" +
                "Adapter not found for types: TestType1 and TestType2");
    }
}