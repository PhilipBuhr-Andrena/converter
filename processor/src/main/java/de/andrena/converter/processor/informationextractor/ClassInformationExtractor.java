package de.andrena.converter.processor.informationextractor;

import de.andrena.annotation.Ignore;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

class ClassInformationExtractor {

    private FieldInformationExtractor fieldInformationExtractor;
    private ClassNameProvider classNameProvider;


    ClassInformationExtractor() {
        this(new FieldInformationExtractor(), new ClassNameProvider());
    }

    ClassInformationExtractor(FieldInformationExtractor fieldInformationExtractor, ClassNameProvider classNameProvider) {
        this.fieldInformationExtractor = fieldInformationExtractor;
        this.classNameProvider = classNameProvider;
    }

    ClassInformation extract(TypeElement element) {
        ClassInformation result = new ClassInformation(classNameProvider.getClassName(element));
        element.getEnclosedElements().stream()
                .filter(child -> child.getKind().isField())
                .filter(this::hasNoIgnoreAnnotation)
                .map(field -> fieldInformationExtractor.extract(field))
                .forEach(result::addField);
        return result;
    }

    private boolean hasNoIgnoreAnnotation(Element field) {
        return field.getAnnotation(Ignore.class) == null;
    }

}
