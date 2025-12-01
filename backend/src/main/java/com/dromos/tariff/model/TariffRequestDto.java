package com.dromos.tariff.model;

public class TariffRequestDto {
    private String hsCode;
    private String totalPrice;
    private String startLocationCode;
    private String endLocationCode;

    // Constructors
    public TariffRequestDto() {}

    public TariffRequestDto(String hsCode, String totalPrice, String startLocationCode, String endLocationCode) {
        this.hsCode = hsCode;
        this.totalPrice = totalPrice;
        this.startLocationCode = startLocationCode;
        this.endLocationCode = endLocationCode;
    }

    // Getters and setters
    public String getHsCode() { return hsCode; }
    public void setHsCode(String hsCode) { this.hsCode = hsCode; }

    public String getTotalPrice() { return totalPrice; }
    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }

    public String getStartLocationCode() { return startLocationCode; }
    public void setStartLocationCode(String startLocationCode) { this.startLocationCode = startLocationCode; }

    public String getEndLocationCode() { return endLocationCode; }
    public void setEndLocationCode(String endLocationCode) { this.endLocationCode = endLocationCode; }
}
