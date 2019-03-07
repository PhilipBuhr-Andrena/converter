package de.andrena.converter.processor.informationextractor;

import de.andrena.annotation.ConversionSource;
import de.andrena.annotation.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AnnotatedClassExtractorTest {

    private static final String NAME = "name";
    private RoundEnvironment roundEnvironment;
    private AnnotatedClassExtractor extractor;
    private TypeElement elementWithConverterAnnotation;
    private TypeElement elementWithConversionSourceAnnotation;
    private TypeElement elementWithDifferentName;
    private ConversionInformationExtractor conversionInformationExtractor;
    private Converter converterAnnotation;

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

        verify(conversionInformationExtractor).extract(elementWithConverterAnnotation, Set.of(elementWithConversionSourceAnnotation), NAME);
    }

    @Test
    void usesElementNameWhenConverterHasEmptyName() {
        when(converterAnnotation.name()).thenReturn("");

        when(roundEnvironment.getElementsAnnotatedWith(Converter.class))
                .thenAnswer(invocation -> Set.of(elementWithConverterAnnotation));
        when(roundEnvironment.getElementsAnnotatedWith(ConversionSource.class))
                .thenAnswer(invocation -> Set.of(elementWithConversionSourceAnnotation, elementWithDifferentName));

        extractor.extract(roundEnvironment);

        verify(conversionInformationExtractor).extract(elementWithConverterAnnotation, Set.of(elementWithConversionSourceAnnotation), NAME);
    }

    @Test
    void throwRuntimeExceptionIfMappingNameIsDuplicate() {
        TypeElement converterElementWithSameMappingName = mock(TypeElement.class);
        when(converterElementWithSameMappingName.getKind()).thenReturn(ElementKind.CLASS);
        when(converterElementWithSameMappingName.getAnnotation(Converter.class)).thenReturn(converterAnnotation);

        when(roundEnvironment.getElementsAnnotatedWith(Converter.class)).thenAnswer(invocation -> Set.of(elementWithConverterAnnotation, converterElementWithSameMappingName));
        when(roundEnvironment.getElementsAnnotatedWith(ConversionSource.class)).thenAnswer(invocation -> Collections.emptySet());

        assertThatThrownBy(() -> extractor.extract(roundEnvironment))
                .isInstanceOf(DuplicateMappingNameException.class)
                .hasMessage("Duplicate Mapping Name found: name");
    }

    private void setUpElements() {
        elementWithConverterAnnotation = mock(TypeElement.class);
        when(elementWithConverterAnnotation.getKind()).thenReturn(ElementKind.CLASS);
        converterAnnotation = mock(Converter.class);
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

        Name name = mock(Name.class);
        when(name.toString()).thenReturn(NAME);
        when(elementWithConverterAnnotation.getSimpleName()).thenReturn(name);
    }
}
