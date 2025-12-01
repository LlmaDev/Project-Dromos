package com.dromos.tariff.service;

import com.dromos.tariff.model.TariffCalculationResponse;
import com.dromos.tariff.model.TariffRequestDto;

public interface TariffCalculator {
    TariffCalculationResponse calculate(TariffRequestDto request);
}