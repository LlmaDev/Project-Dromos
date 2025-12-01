package com.dromos.tariff.service;

import com.dromos.tariff.model.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAllCountries();
    String getCountryNameByCode(String code);
}