/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.processor.informationextractor;

import de.andrena.annotation.ConversionAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConversionMethodExtractorTest {

    private ExecutableElement methodElement;
    private ConversionMethodExtractor conversionMethodExtractor;
    private RoundEnvironment roundEnvironment;

    @BeforeEach
    void setUp() {
        methodElement = mock(ExecutableElement.class);
        when(methodElement.getKind()).thenReturn(ElementKind.METHOD);
        conversionMethodExtractor = new ConversionMethodExtractor();
        roundEnvironment = mock(RoundEnvironment.class);
        when(roundEnvironment.getElementsAnnotatedWith(ConversionAdapter.class))
                .thenAnswer(invocation -> Set.of(methodElement));
    }

    @Test
    void createsConversionMethods() {
        ConversionMethods conversionMethods = conversionMethodExtractor.extract(roundEnvironment);

        assertThat(conversionMethods).isNotNull();
    }

    @Test
    void extractsConversionMethods() {
        ConversionMethods conversionMethods = conversionMethodExtractor.extract(roundEnvironment);

        assertThat(conversionMethods.getMethods().get(0)).isEqualToComparingFieldByField(new ConversionMethod(methodElement));
    }
}