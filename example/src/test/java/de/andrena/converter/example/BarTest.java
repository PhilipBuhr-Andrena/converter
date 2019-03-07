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


    @Test
    void ignoresFieldInSource() {
        BarWithIgnoredField barWithIgnoredField = new BarWithIgnoredField();
        barWithIgnoredField.setName(NAME);
        barWithIgnoredField.setAmount(5);
        barWithIgnoredField.setNumber(3.14);

        Bar bar = BarConverter.createBar(barWithIgnoredField);

        assertThat(bar.getName()).isNull();
        assertThat(bar.getAmount()).isEqualTo(5);
        assertThat(bar.getNumber()).isEqualTo(3.14);
    }

    @Test
    void ignoresFieldInTarget() {
        Bar bar = new Bar();
        bar.setName(NAME);
        bar.setAmount(5);
        bar.setNumber(3.14);

        BarWithIgnoredField barWithIgnoredField = BarConverter.createBarWithIgnoredField(bar);

        assertThat(barWithIgnoredField.getName()).isNull();
        assertThat(barWithIgnoredField.getAmount()).isEqualTo(5);
        assertThat(barWithIgnoredField.getNumber()).isEqualTo(3.14);

    }
}