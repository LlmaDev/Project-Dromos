package com.dromos.tariff.model;

public class Tariff {
    private String origin;
    private String destination;
    private String hsCode;
    private double rate;
    private String currency;

    public Tariff(String origin, String destination, String hsCode, double rate, String currency) {
        this.origin = origin;
        this.destination = destination;
        this.hsCode = hsCode;
        this.rate = rate;
        this.currency = currency;
    }

    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getHsCode() { return hsCode; }
    public double getRate() { return rate; }
    public String getCurrency() { return currency; }
}
