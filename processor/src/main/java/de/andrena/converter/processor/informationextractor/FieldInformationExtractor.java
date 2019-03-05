package de.andrena.converter.processor.informationextractor;

import de.andrena.annotation.Mapping;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

class FieldInformationExtractor {
    FieldInformation extract(Element field) {
        String name = field.getSimpleName().toString();
        boolean isPublic = isPublic(field);
        Mapping mapping = field.getAnnotation(Mapping.class);
        if (mapping == null) {
            return new FieldInformation(name, isPublic);
        }
        return new FieldInformation(name, isPublic, mapping.value());
    }

    private boolean isPublic(Element field) {
        return field.getModifiers().contains(Modifier.PUBLIC);
    }
}
