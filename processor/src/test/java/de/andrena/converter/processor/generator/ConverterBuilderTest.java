/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.processor.generator;

import com.squareup.javapoet.MethodSpec;
import de.andrena.converter.processor.informationextractor.ClassInformation;
import de.andrena.converter.processor.informationextractor.ConversionInformation;
import de.andrena.converter.processor.informationextractor.ConversionMethods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ConverterBuilderTest {


    private static final String PACKAGE_NAME = "com.example";
    private static final String TEST_NAME = "TestName";
    private static final String ERROR_MESSAGE = "Failed to write Converter for Class: TestName";
    private ConversionInformation conversionInformation;
    private MethodBuilder methodBuilder;
    private ConverterBuilder converterBuilder;
    private ClassInformation model;
    private ClassInformation source;
    private ConversionMethods conversionMethods;
    private JavaFileWrapper javaFileWrapper;
    private Messager messager;

    @BeforeEach
    void setUp() {
        model = mock(ClassInformation.class);
        when(model.getSimpleName()).thenReturn(TEST_NAME);
        source = new ClassInformation();

        conversionMethods = mock(ConversionMethods.class);

        conversionInformation = mock(ConversionInformation.class);
        when(conversionInformation.getPackageName()).thenReturn(PACKAGE_NAME);
        when(conversionInformation.getModel()).thenReturn(model);

        methodBuilder = mock(MethodBuilder.class);
        when(methodBuilder.generateConversionMethod(any(), any(), eq(conversionMethods)))
                .thenReturn(MethodSpec.methodBuilder("testMethodName").build());

        javaFileWrapper = mock(JavaFileWrapper.class);
        converterBuilder = new ConverterBuilder(methodBuilder, javaFileWrapper);
        messager = mock(Messager.class);
        converterBuilder.setMessager(messager);
    }

    @Test
    void callsMethodBuilderTwiceForEachSourceClass() {
        when(conversionInformation.getModel()).thenReturn(model);
        when(conversionInformation.getSources()).thenReturn(Collections.singletonList(source));

        converterBuilder.generate(conversionInformation, conversionMethods);
        verify(methodBuilder).generateConversionMethod(model, source, conversionMethods);
        verify(methodBuilder).generateConversionMethod(source, model, conversionMethods);
    }

    @Test
    void javaFileIsWritten() throws IOException {
        converterBuilder.generate(conversionInformation, conversionMethods);
        verify(javaFileWrapper).createJavaFile(eq(PACKAGE_NAME), any(), any());
    }

    @Test
    void logsIfFailedToWriteFile() throws IOException {
        doThrow(new IOException()).when(javaFileWrapper).createJavaFile(any(), any(), any());

        assertThatThrownBy(() -> converterBuilder.generate(conversionInformation, conversionMethods))
                .isInstanceOf(FileWritingException.class)
                .hasMessage(ERROR_MESSAGE);
        verify(messager).printMessage(Diagnostic.Kind.ERROR, ERROR_MESSAGE);
    }

}