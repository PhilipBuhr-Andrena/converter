/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.processor.informationextractor;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.TypeElement;

class ClassNameProvider {
    ClassName getClassName(TypeElement element) {
        return ClassName.get(element);
    }
}
