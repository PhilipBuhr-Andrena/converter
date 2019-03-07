package de.andrena.converter.processor.informationextractor;

import de.andrena.annotation.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.TypeElement;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConversionInformationExtractorTest {

    private static final String NAME = "name";
    private TypeElement model;
    private TypeElement source;
    private ConversionInformationExtractor extractor;
    private ClassInformationExtractor classInformationExtractor;
    private TypeElement source2;
    private Converter converterAnnotation;

    @BeforeEach
    void setUp() {
        model = mock(TypeElement.class);
        converterAnnotation = mock(Converter.class);
        when(converterAnnotation.name()).thenReturn(NAME);
        when(model.getAnnotation(Converter.class)).thenReturn(converterAnnotation);
        source = mock(TypeElement.class);
        source2 = mock(TypeElement.class);

        classInformationExtractor = mock(ClassInformationExtractor.class);
        extractor = new ConversionInformationExtractor(classInformationExtractor);
    }

    @Test
    void usesGivenName() {
        ConversionInformation result = extractor.extract(model, Set.of(source), NAME);

        assertThat(result.getName()).isEqualTo(NAME);
    }


    @Test
    void extractsClassInformationOfModel() {
        ClassInformation expected = new ClassInformation();
        when(classInformationExtractor.extract(model)).thenReturn(expected);
        ConversionInformation result = extractor.extract(model, Set.of(source), NAME);

        assertThat(result.getModel()).isEqualTo(expected);
    }

    @Test
    void extractsClassInformationOfSources() {
        ClassInformation expected = new ClassInformation();
        when(classInformationExtractor.extract(any())).thenReturn(expected);

        ConversionInformation result = extractor.extract(model, Set.of(source, source2), NAME);

        assertThat(result.getSources()).hasSize(2);
        assertThat(result.getSources()).contains(expected);
    }
}