package de.andrena.converter.example;

import de.andrena.annotation.ConversionSource;

@SuppressWarnings("WeakerAccess")
@ConversionSource(name = "DateFoo")
public class FooWithDateString {

    public String date;

    public FooWithDateString() {
    }

    public FooWithDateString(String date) {
        this.date = date;
    }
}
