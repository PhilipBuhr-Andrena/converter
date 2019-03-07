package de.andrena.converter.example;

import de.andrena.annotation.Converter;

import java.time.LocalDate;

@SuppressWarnings("WeakerAccess")
@Converter(name = "DateFoo")
public class FooWithDate {
    private LocalDate date;

    public FooWithDate() {
    }

    FooWithDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
