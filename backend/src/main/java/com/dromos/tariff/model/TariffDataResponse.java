package com.dromos.tariff.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TariffDataResponse {
    @JsonProperty("Dataset")
    private List<TariffData> Dataset;

    // Constructor
    public TariffDataResponse() {}

    // Getters and setters
    public List<TariffData> getDataset() { return Dataset; }
    public void setDataset(List<TariffData> dataset) { Dataset = dataset; }
}
