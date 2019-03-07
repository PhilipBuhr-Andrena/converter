/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.processor.generator;

import com.squareup.javapoet.ClassName;
import de.andrena.converter.processor.informationextractor.ClassInformation;
import de.andrena.converter.processor.informationextractor.ConversionMethods;
import de.andrena.converter.processor.informationextractor.FieldInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

class MethodBuilderTest {

    private static final String NAME = "name";
    private static final String CLASS_NAME = "className";
    private StatementBuilder statementBuilder;
    private ClassInformation to;
    private ClassInformation from;
    private MethodBuilder methodBuilder;
    private FieldInformation fromField;
    private FieldInformation toField;
    private FieldInformation additionalField;
    private ConversionMethods conversionMethods;

    @BeforeEach
    void setUp() {
        statementBuilder = mock(StatementBuilder.class);

        to = mock(ClassInformation.class);

        toField = new FieldInformation(NAME, null, true);
        when(to.getFields()).thenAnswer(invocation -> Collections.singletonList(toField));
        when(to.getSimpleName()).thenReturn(CLASS_NAME);
        when(to.getClassName()).thenReturn(ClassName.get(TestClass.class));

        from = mock(ClassInformation.class);
        fromField = new FieldInformation(NAME, null, true);
        when(from.getFields()).thenAnswer(invocation -> Collections.singletonList(fromField));
        when(from.getClassName()).thenReturn(ClassName.get(TestClassDto.class));
        when(from.findField(toField)).thenReturn(Optional.of(fromField));


        additionalField = new FieldInformation("otherName", null, true);
        from.addField(additionalField);

        methodBuilder = new MethodBuilder(statementBuilder);
        conversionMethods = mock(ConversionMethods.class);
    }

    @Test
    void findsCorrespondingFields() {
        methodBuilder.generateConversionMethod(to, from, conversionMethods);
        verify(statementBuilder).mapField(toField, fromField, conversionMethods);
        verify(statementBuilder, never()).mapField(toField, additionalField, conversionMethods);
    }

    private static class TestClass {
    }

    private static class TestClassDto {
    }

}