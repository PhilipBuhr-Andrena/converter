/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.example;

import de.andrena.annotation.ConversionSource;
import de.andrena.annotation.Mapping;

@SuppressWarnings("WeakerAccess")
@ConversionSource(name = "Foo")
public class FooWithDifferentFieldName {

    @Mapping("name")
    public String otherName;
}
