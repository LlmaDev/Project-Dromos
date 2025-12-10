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
        List<Country> countries = wtoApiClient.fetchCountries();
        
        // Atualiza o cache
        updateCache(countries);
        
        // Retorna apenas code e name conforme solicitado
        return countries.stream()
                .map(country -> new Country(country.getCode(), country.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getCountryNameByCode(String code) {
        // Verifica se o código é nulo ou vazio
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        
        // Verifica se o cache foi inicializado
        if (countryCache == null) {
            return null;
        }
        
        // Retorna o nome do país do cache (ou null se não encontrado)
        return countryCache.get(code);
    }


    private void updateCache(List<Country> countries) {
        countryCache = countries.stream()
                .collect(Collectors.toMap(
                    Country::getCode, 
                    Country::getName,
                    (existing, replacement) -> existing, // Em caso de duplicata, manter existente
                    HashMap::new
                ));
    }
}