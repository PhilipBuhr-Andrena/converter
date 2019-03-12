/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.example;

import de.andrena.annotation.ConversionSource;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@ConversionSource(name = "WithList")
public class WithListDto {

    public List<String> list;
}
