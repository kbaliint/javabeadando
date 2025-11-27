package com.beadando.forexapp.service;

public class Transaction {

    private String date;
    private String type;
    private String currency;
    private double amount;
    private double balanceAfter;

    public Transaction(String date, String type, String currency, double amount, double balanceAfter) {
        this.date = date;
        this.type = type;
        this.currency = currency;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    public String getDate() { return date; }
    public String getType() { return type; }
    public String getCurrency() { return currency; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
}
