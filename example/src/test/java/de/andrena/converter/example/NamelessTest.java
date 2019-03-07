/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NamelessTest {

    @Test
    void converterNameIsOptional() {
        NamelessDto namelessDto = new NamelessDto();

        Nameless nameless = NamelessConverter.createNameless(namelessDto);

        assertThat(nameless).isNotNull();
    }
}