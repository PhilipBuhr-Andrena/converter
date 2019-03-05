package de.andrena.converter.processor;

import de.andrena.converter.processor.generator.ConverterBuilder;
import de.andrena.converter.processor.informationextractor.AnnotatedClassExtractor;
import de.andrena.converter.processor.informationextractor.ConversionInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.*;

class ConverterProcessorTest {

    private RoundEnvironment roundEnvironment;
    private AnnotatedClassExtractor annotatedClassExtractor;
    private ConverterProcessor converterProcessor;
    private ConverterBuilder converterBuilder;
    private ProcessingEnvironment processingEnv;
    private Filer filer;
    private Messager messager;

    @BeforeEach
    void setUp() {
        roundEnvironment = mock(RoundEnvironment.class);
        annotatedClassExtractor = mock(AnnotatedClassExtractor.class);
        converterBuilder = mock(ConverterBuilder.class);

        converterProcessor = new ConverterProcessor();
        converterProcessor.setAnnotatedClassExtractor(annotatedClassExtractor);
        converterProcessor.setConverterBuilder(converterBuilder);

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
        when(annotatedClassExtractor.extract(roundEnvironment)).thenReturn(Collections.singletonList(conversionInformation));
        converterProcessor.process(Collections.emptySet(), roundEnvironment);
        verify(converterBuilder).generate(conversionInformation);
    }

    @Test
    void initializesConverterBuilderDependencies() {
        verify(converterBuilder).setFiler(filer);
        verify(converterBuilder).setMessager(messager);
    }
}