/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.processor;

import de.andrena.converter.processor.generator.ConverterBuilder;
import de.andrena.converter.processor.informationextractor.AnnotatedClassExtractor;
import de.andrena.converter.processor.informationextractor.ConversionInformation;
import de.andrena.converter.processor.informationextractor.ConversionMethodExtractor;
import de.andrena.converter.processor.informationextractor.ConversionMethods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ConverterProcessorTest {

    private RoundEnvironment roundEnvironment;
    private AnnotatedClassExtractor annotatedClassExtractor;
    private ConverterProcessor converterProcessor;
    private ConverterBuilder converterBuilder;
    private ProcessingEnvironment processingEnv;
    private Filer filer;
    private Messager messager;
    private ConversionMethodExtractor conversionMethodExtractor;

    @BeforeEach
    void setUp() {
        roundEnvironment = mock(RoundEnvironment.class);
        annotatedClassExtractor = mock(AnnotatedClassExtractor.class);
        converterBuilder = mock(ConverterBuilder.class);
        conversionMethodExtractor = mock(ConversionMethodExtractor.class);

        converterProcessor = new ConverterProcessor();
        converterProcessor.setAnnotatedClassExtractor(annotatedClassExtractor);
        converterProcessor.setConverterBuilder(converterBuilder);
        converterProcessor.setConversionMethodExtractor(conversionMethodExtractor);

        processingEnv = mock(ProcessingEnvironment.class);
        filer = mock(Filer.class);
        messager = mock(Messager.class);
        when(processingEnv.getFiler()).thenReturn(filer);
        when(processingEnv.getMessager()).thenReturn(messager);
        converterProcessor.init(processingEnv);
    }

    @Test
    void extractsRelevantInformation() {
        converterProcessor.process(Collections.emptySet(), roundEnvironment);
        verify(annotatedClassExtractor).extract(roundEnvironment);
    }

    @Test
    void passesInformationToConverterBuilder() {
        ConversionInformation conversionInformation = new ConversionInformation();
        ConversionMethods conversionMethods = new ConversionMethods(Collections.emptyList());

        when(annotatedClassExtractor.extract(roundEnvironment)).thenReturn(Collections.singletonList(conversionInformation));
        when(conversionMethodExtractor.extract(roundEnvironment)).thenReturn(conversionMethods);

        converterProcessor.process(Collections.emptySet(), roundEnvironment);
        verify(converterBuilder).generate(conversionInformation, conversionMethods);
    }

    @Test
    void initializesConverterBuilderDependencies() {
        verify(converterBuilder).setFiler(filer);
        verify(converterBuilder).setMessager(messager);
    }

    @Test
    void extractsConversionMethods() {
        converterProcessor.process(Collections.emptySet(), roundEnvironment);
        verify(conversionMethodExtractor).extract(roundEnvironment);
    }

    @Test
    void supportsRelevantAnnotations() {
        Set<String> supportedAnnotationTypes = converterProcessor.getSupportedAnnotationTypes();
        assertThat(supportedAnnotationTypes).contains(
                "de.andrena.annotation.Converter",
                "de.andrena.annotation.ConversionSource",
                "de.andrena.annotation.ConversionAdapter"
        );
    }
}