/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.example;

import de.andrena.annotation.ConversionSource;
import de.andrena.annotation.Ignore;

@SuppressWarnings("WeakerAccess")
@ConversionSource(name = "Bar")
public class BarWithIgnoredField {

    @Ignore
    private String name;
    private int amount;
    private double number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }
}
