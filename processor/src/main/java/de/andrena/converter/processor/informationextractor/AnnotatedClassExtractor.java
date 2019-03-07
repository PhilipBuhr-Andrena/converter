/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.processor.informationextractor;

import com.google.common.base.Strings;
import de.andrena.annotation.ConversionSource;
import de.andrena.annotation.Converter;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.lang.model.element.ElementKind.CLASS;

public class AnnotatedClassExtractor {
    private Messager messager;
    private ConversionInformationExtractor conversionInformationExtractor;

    public AnnotatedClassExtractor() {
        this(new ConversionInformationExtractor());
    }

    AnnotatedClassExtractor(ConversionInformationExtractor conversionInformationExtractor) {
        this.conversionInformationExtractor = conversionInformationExtractor;
    }

    public List<ConversionInformation> extract(RoundEnvironment roundEnvironment) {
        Set<TypeElement> targetElements = extractTypeElements(roundEnvironment, Converter.class);
        validateTargets(targetElements);
        Set<TypeElement> sourceClasses = extractTypeElements(roundEnvironment, ConversionSource.class);

        return targetElements.stream()
                .map(targetElement -> createConversionInformation(sourceClasses, targetElement))
                .collect(Collectors.toList());
    }

    private void validateTargets(Set<TypeElement> targetElements) {
        Set<String> mappingNames = new HashSet<>();
        targetElements.forEach(typeElement -> validateMappingName(typeElement, mappingNames));
    }

    private ConversionInformation createConversionInformation(Set<TypeElement> sourceClasses, TypeElement targetElement) {
        String name = findMappingName(targetElement);
        Set<TypeElement> sources = findSources(sourceClasses, name);
        return conversionInformationExtractor.extract(targetElement, sources, name);
    }

    private Set<TypeElement> findSources(Set<TypeElement> sourceClasses, String name) {
        return sourceClasses.stream()
                .filter(typeElement -> typeElement.getAnnotation(ConversionSource.class).name().equals(name))
                .collect(Collectors.toSet());
    }

    private String findMappingName(TypeElement targetElement) {
        String name = targetElement.getAnnotation(Converter.class).name();
        if (Strings.isNullOrEmpty(name)) {
            return targetElement.getSimpleName().toString();
        }
        return name;
    }

    private Set<TypeElement> extractTypeElements(RoundEnvironment roundEnvironment, Class<? extends Annotation> annotation) {
        return roundEnvironment.getElementsAnnotatedWith(annotation).stream()
                .filter(element -> element.getKind().equals(CLASS))
                .map(element -> (TypeElement) element)
                .peek(typeElement -> logFoundClass(typeElement, annotation))
                .collect(Collectors.toSet());
    }

    private void validateMappingName(TypeElement typeElement, Set<String> mappingNames) {
        String mappingName = findMappingName(typeElement);
        if (mappingNames.contains(mappingName)) {
            throw new DuplicateMappingNameException("Duplicate Mapping Name found: " + mappingName);
        }
        mappingNames.add(mappingName);
    }

    private void logFoundClass(TypeElement typeElement, Class<? extends Annotation> annotation) {
        if (messager != null) {
            messager.printMessage(Diagnostic.Kind.NOTE, "Found Annotated Class: " + typeElement.getSimpleName() + ", " + annotation.getName());
        }
    }

    public void setMessager(Messager messager) {
        this.messager = messager;
    }
}
