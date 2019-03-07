package de.andrena.converter.processor.informationextractor;

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
                .map(field -> fieldInformationExtractor.extract(field))
                .forEach(result::addField);
        return result;
    }

}
