package de.andrena.converter.processor.informationextractor;

import de.andrena.annotation.ConversionSource;
import de.andrena.annotation.Converter;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
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
        Set<TypeElement> sourceClasses = extractTypeElements(roundEnvironment, ConversionSource.class);


        List<ConversionInformation> result = new ArrayList<>();
        for (TypeElement targetElement : targetElements) {
            String name = targetElement.getAnnotation(Converter.class).name();

            Set<TypeElement> sources = sourceClasses.stream()
                    .filter(typeElement -> typeElement.getAnnotation(ConversionSource.class).name().equals(name))
                    .collect(Collectors.toSet());
            result.add(conversionInformationExtractor.extract(targetElement, sources));
        }
        return result;
    }

    private Set<TypeElement> extractTypeElements(RoundEnvironment roundEnvironment, Class<? extends Annotation> annotation) {
        return roundEnvironment.getElementsAnnotatedWith(annotation).stream()
                .filter(element -> element.getKind().equals(CLASS))
                .map(element -> (TypeElement) element)
                .peek(typeElement -> logFoundClass(typeElement, annotation))
                .collect(Collectors.toSet());
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
