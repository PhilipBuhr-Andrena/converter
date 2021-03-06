/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.processor.informationextractor;

import de.andrena.annotation.Mapping;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

class FieldInformationExtractor {
    FieldInformation extract(Element field) {
        String name = field.getSimpleName().toString();
        TypeMirror type = field.asType();
        boolean isPublic = isPublic(field);
        Mapping mapping = field.getAnnotation(Mapping.class);
        if (mapping == null) {
            return new FieldInformation(name, type, isPublic);
        }
        return new FieldInformation(name, type, isPublic, mapping.value());
    }

    private boolean isPublic(Element field) {
        return field.getModifiers().contains(Modifier.PUBLIC);
    }
}
