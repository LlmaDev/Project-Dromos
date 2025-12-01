package com.dromos.tariff.service;

import com.dromos.tariff.model.Country;
import com.dromos.tariff.model.TariffData;

import java.util.List;

public interface WtoApiClient {
    List<Country> fetchCountries();
    List<TariffData> fetchTariffData(String hsCode, List<String> countryCodes);
}