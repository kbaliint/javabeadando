package com.beadando.forexapp.service;

public class Position {

    private String instrument;
    private String side;
    private double units;
    private double openPrice;

    public Position(String instrument, String side, double units, double openPrice) {
        this.instrument = instrument;
        this.side = side;
        this.units = units;
        this.openPrice = openPrice;
    }

    public String getInstrument() { return instrument; }
    public String getSide() { return side; }
    public double getUnits() { return units; }
    public double getOpenPrice() { return openPrice; }
}
