/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WithListTest {

    @Test
    void convertsListsWithSameComponent() {
        WithListDto withListDto = new WithListDto();
        withListDto.list = new ArrayList<>();
        withListDto.list.add("string");

        WithList withList = WithListConverter.createWithList(withListDto);
        assertThat(withList.list).contains("string");
        assertThat(withList.list).hasSize(1);
    }

    @Test
    void convertsListWithDifferentComponents() {
        List<Foo> foos = Collections.singletonList(new Foo("name"));
        WithListOtherType withListOtherType = new WithListOtherType(foos);

        WithList withList = WithListConverter.createWithList(withListOtherType);

        assertThat(withList.list).contains("name");
        assertThat(withList.list).hasSize(1);
    }
}