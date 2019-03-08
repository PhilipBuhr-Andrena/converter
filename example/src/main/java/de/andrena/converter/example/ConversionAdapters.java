/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.example;

import de.andrena.annotation.ConversionAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("WeakerAccess")
public final class ConversionAdapters {

    private ConversionAdapters() {
    }

    @ConversionAdapter
    public static String convertDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @ConversionAdapter
    public static LocalDate covertToDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
