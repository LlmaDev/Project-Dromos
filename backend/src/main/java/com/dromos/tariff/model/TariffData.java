package com.dromos.tariff.model;

public class TariffData {
    private String ReportingEconomyCode;
    private String ReportingEconomy;
    private String ProductOrSectorCode;
    private String ProductOrSector;
    private Integer Year;
    private Double Value;
    private String Unit;
    private String UnitCode;

    // Constructors
    public TariffData() {}

    // Getters and setters
    public String getReportingEconomyCode() { return ReportingEconomyCode; }
    public void setReportingEconomyCode(String reportingEconomyCode) { ReportingEconomyCode = reportingEconomyCode; }

    public String getReportingEconomy() { return ReportingEconomy; }
    public void setReportingEconomy(String reportingEconomy) { ReportingEconomy = reportingEconomy; }

    public String getProductOrSectorCode() { return ProductOrSectorCode; }
    public void setProductOrSectorCode(String productOrSectorCode) { ProductOrSectorCode = productOrSectorCode; }

    public String getProductOrSector() { return ProductOrSector; }
    public void setProductOrSector(String productOrSector) { ProductOrSector = productOrSector; }

    public Integer getYear() { return Year; }
    public void setYear(Integer year) { Year = year; }

    public Double getValue() { return Value; }
    public void setValue(Double value) { Value = value; }

    public String getUnit() { return Unit; }
    public void setUnit(String unit) { Unit = unit; }

    public String getUnitCode() { return UnitCode; }
    public void setUnitCode(String unitCode) { UnitCode = unitCode; }
}
