package de.andrena.converter.example;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ConversionAdaptersTest {

    private static final LocalDate DATE = LocalDate.of(2019, 3, 4);
    private static final String DATE_STRING = ConversionAdapters.convertDate(DATE);

    @Test
    void convertLocalDateToISOString() {
        assertThat(DATE_STRING).isEqualTo("2019-03-04");
    }

    @Test
    void convertStringToLocalDate() {
        assertThat(ConversionAdapters.covertToDate(DATE_STRING)).isEqualTo(DATE);
    }
}