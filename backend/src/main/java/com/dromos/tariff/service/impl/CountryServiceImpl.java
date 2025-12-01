package com.dromos.tariff.service.impl;

import com.dromos.tariff.model.Country;
import com.dromos.tariff.service.CountryService;
import com.dromos.tariff.service.WtoApiClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService {

    private final WtoApiClient wtoApiClient;
    private Map<String, String> countryCache;

    public CountryServiceImpl(WtoApiClient wtoApiClient) {
        this.wtoApiClient = wtoApiClient;
    }

    @Override
    public List<Country> getAllCountries() {
        // todo
        return null;
    }

    @Override
    public String getCountryNameByCode(String code) {
       // todo
        return null;
    }

}