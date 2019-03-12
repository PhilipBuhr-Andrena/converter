/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.example;

import de.andrena.annotation.ConversionAdapter;
import de.andrena.annotation.ConversionSource;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
@ConversionSource(name = "WithList")
public class WithListOtherType {

    public List<Foo> list;

    public WithListOtherType() {
    }

    public WithListOtherType(List<Foo> list) {
        this.list = list;
    }

    @ConversionAdapter
    public static List<String> convertFoosToStrings(List<Foo> foos) {
        return foos.stream()
                .map(foo -> foo.name)
                .collect(Collectors.toList());
    }

    @ConversionAdapter
    public static List<Foo> convertToFoos(List<String> names) {
        return names.stream()
                .map(Foo::new)
                .collect(Collectors.toList());
    }
}
