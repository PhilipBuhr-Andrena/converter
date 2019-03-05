package de.andrena.converter.processor.informationextractor;

import de.andrena.annotation.ConversionSource;
import de.andrena.annotation.Converter;
import de.andrena.converter.processor.informationextractor.AnnotatedClassExtractor;
import de.andrena.converter.processor.informationextractor.ConversionInformationExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static org.mockito.Mockito.*;

class AnnotatedClassExtractorTest {

    private static final String NAME = "name";
    private RoundEnvironment roundEnvironment;
    private AnnotatedClassExtractor extractor;
    private TypeElement elementWithConverterAnnotation;
    private TypeElement elementWithConversionSourceAnnotation;
    private TypeElement elementWithDifferentName;
    private ConversionInformationExtractor conversionInformationExtractor;

    @BeforeEach
    void setUp() {
        roundEnvironment = mock(RoundEnvironment.class);
        conversionInformationExtractor = mock(ConversionInformationExtractor.class);

        extractor = new AnnotatedClassExtractor(conversionInformationExtractor);

        setUpElements();
    }

    @Test
    void extractsAnnotatedClasses() {
        extractor.extract(roundEnvironment);
        verify(roundEnvironment).getElementsAnnotatedWith(Converter.class);
        verify(roundEnvironment).getElementsAnnotatedWith(ConversionSource.class);
    }

    @Test
    void groupsElementsByName() {
        when(roundEnvironment.getElementsAnnotatedWith(Converter.class))
                .thenAnswer(invocation -> Set.of(elementWithConverterAnnotation));
        when(roundEnvironment.getElementsAnnotatedWith(ConversionSource.class))
                .thenAnswer(invocation -> Set.of(elementWithConversionSourceAnnotation, elementWithDifferentName));
        extractor.extract(roundEnvironment);

        verify(conversionInformationExtractor).extract(elementWithConverterAnnotation, Set.of(elementWithConversionSourceAnnotation));
    }

    private void setUpElements() {
        elementWithConverterAnnotation = mock(TypeElement.class);
        when(elementWithConverterAnnotation.getKind()).thenReturn(ElementKind.CLASS);
        Converter converterAnnotation = mock(Converter.class);
        when(converterAnnotation.name()).thenReturn(NAME);
        when(elementWithConverterAnnotation.getAnnotation(Converter.class)).thenReturn(converterAnnotation);

        elementWithConversionSourceAnnotation = mock(TypeElement.class);
        when(elementWithConversionSourceAnnotation.getKind()).thenReturn(ElementKind.CLASS);
        ConversionSource conversionSourceAnnotation = mock(ConversionSource.class);
        when(conversionSourceAnnotation.name()).thenReturn(NAME);
        when(elementWithConversionSourceAnnotation.getAnnotation(ConversionSource.class)).thenReturn(conversionSourceAnnotation);

        elementWithDifferentName = mock(TypeElement.class);
        when(elementWithDifferentName.getKind()).thenReturn(ElementKind.CLASS);
        ConversionSource conversionSourceAnnotationWithDifferentName = mock(ConversionSource.class);
        when(conversionSourceAnnotationWithDifferentName.name()).thenReturn("otherName");
        when(elementWithDifferentName.getAnnotation(ConversionSource.class)).thenReturn(conversionSourceAnnotationWithDifferentName);
    }
}