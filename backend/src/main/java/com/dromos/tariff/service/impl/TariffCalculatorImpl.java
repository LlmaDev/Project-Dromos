package com.dromos.tariff.service.impl;

import com.dromos.tariff.model.*;
import com.dromos.tariff.service.CountryService;
import com.dromos.tariff.service.TariffCalculator;
import com.dromos.tariff.service.WtoApiClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TariffCalculatorImpl implements TariffCalculator {

    private final WtoApiClient wtoApiClient;
    private final CountryService countryService;

    public TariffCalculatorImpl(WtoApiClient wtoApiClient, CountryService countryService) {
        this.wtoApiClient = wtoApiClient;
        this.countryService = countryService;
    }

    @Override
    public TariffCalculationResponse calculate(TariffRequestDto request) {
        // todo
        return null;
    }
}