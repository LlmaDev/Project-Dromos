package com.dromos.tariff.model;

public class Country {
    private String code;
    private String name;
    private String iso3A;
    private Integer displayOrder;

    // Constructors
    public Country() {}

    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Country(String code, String name, String iso3A, Integer displayOrder) {
        this.code = code;
        this.name = name;
        this.iso3A = iso3A;
        this.displayOrder = displayOrder;
    }

    // Getters and setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIso3A() { return iso3A; }
    public void setIso3A(String iso3A) { this.iso3A = iso3A; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
}
