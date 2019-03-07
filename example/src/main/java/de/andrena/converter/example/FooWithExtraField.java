/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.example;

import de.andrena.annotation.ConversionSource;

@SuppressWarnings("WeakerAccess")
@ConversionSource(name = "Foo")
public class FooWithExtraField {

    public String name;

    public int extraField;
}
