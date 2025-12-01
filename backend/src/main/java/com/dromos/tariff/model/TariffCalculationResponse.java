package com.dromos.tariff.model;

import java.util.List;

public class TariffCalculationResponse {
    private String hsCode;
    private String totalPrice;
    private String startLocationCode;
    private String endLocationCode;
    private String startLocationName;
    private String endLocationName;
    private List<TariffDetail> tariffDetails;
    private Double totalTariff;
    private Double finalPrice;

    // Constructors
    public TariffCalculationResponse() {}

    // Getters and setters
    public String getHsCode() { return hsCode; }
    public void setHsCode(String hsCode) { this.hsCode = hsCode; }

    public String getTotalPrice() { return totalPrice; }
    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }

    public String getStartLocationCode() { return startLocationCode; }
    public void setStartLocationCode(String startLocationCode) { this.startLocationCode = startLocationCode; }

    public String getEndLocationCode() { return endLocationCode; }
    public void setEndLocationCode(String endLocationCode) { this.endLocationCode = endLocationCode; }

    public String getStartLocationName() { return startLocationName; }
    public void setStartLocationName(String startLocationName) { this.startLocationName = startLocationName; }

    public String getEndLocationName() { return endLocationName; }
    public void setEndLocationName(String endLocationName) { this.endLocationName = endLocationName; }

    public List<TariffDetail> getTariffDetails() { return tariffDetails; }
    public void setTariffDetails(List<TariffDetail> tariffDetails) { this.tariffDetails = tariffDetails; }

    public Double getTotalTariff() { return totalTariff; }
    public void setTotalTariff(Double totalTariff) { this.totalTariff = totalTariff; }

    public Double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(Double finalPrice) { this.finalPrice = finalPrice; }

    public static class TariffDetail {
        private String countryCode;
        private String countryName;
        private Double tariffRate;
        private Double calculatedTariff;

        public TariffDetail() {}

        public TariffDetail(String countryCode, String countryName, Double tariffRate, Double calculatedTariff) {
            this.countryCode = countryCode;
            this.countryName = countryName;
            this.tariffRate = tariffRate;
            this.calculatedTariff = calculatedTariff;
        }

        public String getCountryCode() { return countryCode; }
        public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

        public String getCountryName() { return countryName; }
        public void setCountryName(String countryName) { this.countryName = countryName; }

        public Double getTariffRate() { return tariffRate; }
        public void setTariffRate(Double tariffRate) { this.tariffRate = tariffRate; }

        public Double getCalculatedTariff() { return calculatedTariff; }
        public void setCalculatedTariff(Double calculatedTariff) { this.calculatedTariff = calculatedTariff; }
    }
}
