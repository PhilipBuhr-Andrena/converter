package de.andrena.converter.example;

import de.andrena.annotation.ConversionSource;
import de.andrena.annotation.Mapping;
import jdk.jfr.MemoryAddress;

@ConversionSource(name = "Foo")
public class FooWithDifferentFieldName {

    @Mapping("name")
    public String otherName;
}
