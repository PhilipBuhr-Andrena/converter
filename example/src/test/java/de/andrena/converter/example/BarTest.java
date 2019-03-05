package de.andrena.converter.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BarTest {

    private static final String NAME = "name";

    @Test
    void mapsMultipleFieldsCorrectly() {
        BarDto barDto = new BarDto();
        barDto.setName(NAME);
        barDto.setAmount(5);
        barDto.setNumber(3.14);

        Bar bar = BarConverter.createBar(barDto);

        assertThat(bar.getName()).isEqualTo(NAME);
        assertThat(bar.getAmount()).isEqualTo(5);
        assertThat(bar.getNumber()).isEqualTo(3.14);
    }
}