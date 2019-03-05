package de.andrena.converter.example;

import de.andrena.annotation.Converter;

@Converter(name = "Bar")
public class Bar {

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
