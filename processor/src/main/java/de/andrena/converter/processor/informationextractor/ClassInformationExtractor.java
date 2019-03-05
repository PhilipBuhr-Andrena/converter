package de.andrena.converter.processor.informationextractor;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.TypeElement;

class ClassInformationExtractor {

    private FieldInformationExtractor fieldInformationExtractor;

    ClassInformationExtractor() {
        this(new FieldInformationExtractor());
    }

    private ClassInformationExtractor(FieldInformationExtractor fieldInformationExtractor) {
        this.fieldInformationExtractor = fieldInformationExtractor;
    }

    ClassInformation extract(TypeElement element) {
        ClassInformation result = new ClassInformation(ClassName.get(element));
        element.getEnclosedElements().stream()
                .filter(child -> child.getKind().isField())
                .map(field -> fieldInformationExtractor.extract(field))
                .forEach(result::addField);
        return result;
    }
}
