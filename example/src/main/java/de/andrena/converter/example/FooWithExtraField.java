package de.andrena.converter.example;

import de.andrena.annotation.ConversionSource;

@ConversionSource(name = "Foo")
public class FooWithExtraField {

    public String name;

    public int extraField;
}
