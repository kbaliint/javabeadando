package com.beadando.forexapp.service;

import java.time.LocalDateTime;

public class Position {

    // ✅ automatikus tradeId számláló
    private static long NEXT_ID = 1;

    private long tradeId;
    private String instrument;
    private String side;
    private double units;
    private double openPrice;
    private LocalDateTime openTime;

    public Position(String instrument, String side, double units, double openPrice) {
        this.tradeId = NEXT_ID++;
        this.instrument = instrument;
        this.side = side;
        this.units = units;
        this.openPrice = openPrice;
        this.openTime = LocalDateTime.now();
    }

    // ✅ GETTEREK

    public long getTradeId() {
        return tradeId;
    }

    public String getInstrument() {
        return instrument;
    }

    public String getSide() {
        return side;
    }

    public double getUnits() {
        return units;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public LocalDateTime getOpenTime() {
        return openTime;
    }
}
