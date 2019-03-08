/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.example;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ConversionAdaptersTest {

    private static final LocalDate DATE = LocalDate.of(2019, 3, 4);
    private static final String DATE_STRING = "04/03/2019";

    @Test
    void convertLocalDateToISOString() {
        assertThat(ConversionAdapters.convertDate(DATE)).isEqualTo(DATE_STRING);
    }

    @Test
    void convertStringToLocalDate() {
        assertThat(ConversionAdapters.covertToDate(DATE_STRING)).isEqualTo(DATE);
    }
}