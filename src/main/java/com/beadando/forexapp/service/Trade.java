package com.beadando.forexapp.service;

public class Trade {

    private String instrument;
    private String side;
    private String date;
    private double price;

    public Trade(String instrument, String side, String date, double price) {
        this.instrument = instrument;
        this.side = side;
        this.date = date;
        this.price = price;
    }

    public String getInstrument() { return instrument; }
    public String getSide() { return side; }
    public String getDate() { return date; }
    public double getPrice() { return price; }
}
