package de.andrena.converter.processor.generator;

import com.squareup.javapoet.MethodSpec;
import de.andrena.converter.processor.informationextractor.ClassInformation;
import de.andrena.converter.processor.informationextractor.ConversionInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

class ConverterBuilderTest {


    private ConversionInformation conversionInformation;
    private MethodBuilder methodBuilder;
    private ConverterBuilder converterBuilder;
    private ClassInformation model;
    private ClassInformation source;

    @BeforeEach
    void setUp() {
        model = new ClassInformation();
        source = new ClassInformation();

        conversionInformation = mock(ConversionInformation.class);
        when(conversionInformation.getName()).thenReturn("TestName");

        methodBuilder = mock(MethodBuilder.class);
        when(methodBuilder.generateConversionMethod(any(), any()))
                .thenReturn(MethodSpec.methodBuilder("testMethodName").build());

        converterBuilder = new ConverterBuilder(methodBuilder);
    }

    @Test
    void callsMethodBuilderTwiceForEachSourceClass() {

        when(conversionInformation.getModel()).thenReturn(model);
        when(conversionInformation.getSources()).thenReturn(Collections.singletonList(source));
        when(conversionInformation.getPackageName()).thenReturn("packageName");

        converterBuilder.generate(conversionInformation);
        verify(methodBuilder).generateConversionMethod(model, source);
        verify(methodBuilder).generateConversionMethod(source, model);
    }
}