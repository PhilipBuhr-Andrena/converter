/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.processor.informationextractor;

import de.andrena.annotation.ConversionAdapter;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import java.util.List;
import java.util.stream.Collectors;

public class ConversionMethodExtractor {
    public ConversionMethods extract(RoundEnvironment roundEnvironment) {
        List<ConversionMethod> conversionMethods = roundEnvironment.getElementsAnnotatedWith(ConversionAdapter.class).stream()
                .filter(element -> element.getKind().equals(ElementKind.METHOD))
                .map(element -> (ExecutableElement) element)
                .map(ConversionMethod::new)
                .collect(Collectors.toList());
        return new ConversionMethods(conversionMethods);
    }
}
