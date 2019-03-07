/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.example;

import de.andrena.annotation.ConversionSource;

@SuppressWarnings("WeakerAccess")
@ConversionSource(name = "Foo")
public class FooDto {
    public String name;

    public FooDto() {
    }

    public FooDto(String name) {
        this.name = name;
    }
}
