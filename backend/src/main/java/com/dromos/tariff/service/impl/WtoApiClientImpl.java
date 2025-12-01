package com.dromos.tariff.service.impl;

import com.dromos.tariff.model.*;
import com.dromos.tariff.service.WtoApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Component
public class WtoApiClientImpl implements WtoApiClient {

    @Value("${wto.api.key}")
    private String wtoApiKey;

    @Value("${wto.api.base-url}")
    private String wtoBaseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WtoApiClientImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Country> fetchCountries() {
        try {
            String url = buildCountriesUrl();
            String response = restTemplate.getForObject(url, String.class);
            
            return objectMapper.readValue(response, new TypeReference<List<Country>>() {});
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar países da API WTO: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<TariffData> fetchTariffData(String hsCode, List<String> countryCodes) {
        if (!validateInput(hsCode, countryCodes)) {
            System.err.println("Input inválido para busca de dados de tarifa.");
            return new ArrayList<>();
        }
        
        try {
            String url = buildTariffDataUrl(hsCode, countryCodes);
            String response = restTemplate.getForObject(url, String.class);
            
            TariffDataResponse tariffResponse = objectMapper.readValue(response, TariffDataResponse.class);
            return tariffResponse.getDataset() != null ? tariffResponse.getDataset() : new ArrayList<>();
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar dados de tarifa da API WTO: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    boolean validateInput(String hsCode, List<String> countryCodes) {
        if (hsCode == null || hsCode.isEmpty()) {
            return false;
        }
        if (countryCodes == null || countryCodes.isEmpty()) {
            return false;
        }

        return hasUniqueCountryCodes(countryCodes);
    }

    private boolean hasUniqueCountryCodes(List<String> countryCodes) {
        //do not allow duplicate country codes
        long distinctCount = countryCodes.stream().distinct().count();
        return distinctCount == countryCodes.size();
    }

    private String buildCountriesUrl() throws Exception {
        return wtoBaseUrl + "/reporters?subscription-key=" + 
            URLEncoder.encode(wtoApiKey, StandardCharsets.UTF_8);
    }

    private String buildTariffDataUrl(String hsCode, List<String> countryCodes) throws Exception {
        int currentYear = Year.now().getValue();
        countryCodes.forEach(code -> URLEncoder.encode(code, StandardCharsets.UTF_8));
        String countryCodesParam = String.join(",", countryCodes);
        return String.format("%s/data?i=HS_A_0010&r=%s&pc=%s&ps=%d&subscription-key=%s",
                wtoBaseUrl,
                countryCodesParam,
                URLEncoder.encode(hsCode, StandardCharsets.UTF_8),
                currentYear - 1,
                URLEncoder.encode(wtoApiKey, StandardCharsets.UTF_8));
    }
}