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
    
    private String buildCountriesUrl() throws Exception {
        return wtoBaseUrl + "/reporters?subscription-key=" + 
            URLEncoder.encode(wtoApiKey, StandardCharsets.UTF_8);
    }
}