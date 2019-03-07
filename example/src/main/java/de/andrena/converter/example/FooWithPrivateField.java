/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.example;

import de.andrena.annotation.ConversionSource;

@SuppressWarnings("WeakerAccess")
@ConversionSource(name = "Foo")
public class FooWithPrivateField {
    private String name;

    public FooWithPrivateField() {
    }

    public FooWithPrivateField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
