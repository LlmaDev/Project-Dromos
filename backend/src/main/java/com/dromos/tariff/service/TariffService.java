package com.dromos.tariff.service;

import com.dromos.tariff.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TariffService {

    private final CountryService countryService;
    private final TariffCalculator tariffCalculator;

    public TariffService(CountryService countryService, TariffCalculator tariffCalculator) {
        this.countryService = countryService;
        this.tariffCalculator = tariffCalculator;
    }

    /**
     * Busca todos os países disponíveis
     * @return Lista de países com código e nome
     */
    public List<Country> getCountries() {
        return countryService.getAllCountries();
    }

}
