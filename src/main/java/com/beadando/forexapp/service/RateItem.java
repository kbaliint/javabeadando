package com.beadando.forexapp.service;

public class RateItem {
    private String currency;
    private int unit;
    private String value;

    public RateItem(String currency, int unit, String value) {
        this.currency = currency;
        this.unit = unit;
        this.value = value;
    }

    public String getCurrency() { return currency; }
    public int getUnit() { return unit; }
    public String getValue() { return value; }

    @Override
    public String toString() {
        return currency + " (" + unit + ") = " + value;
    }
}
