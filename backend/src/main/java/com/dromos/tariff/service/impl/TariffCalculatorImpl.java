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
        TariffCalculationResponse response = buildInitialResponse(request);
        
        try {
            List<TariffData> tariffData = wtoApiClient.fetchTariffData(
                request.getHsCode(), 
                List.of(
                    request.getEndLocationCode(), 
                    request.getStartLocationCode()
                )
            );
            
            // Define nomes dos países
            response.setStartLocationName(countryService.getCountryNameByCode(request.getStartLocationCode()));
            response.setEndLocationName(countryService.getCountryNameByCode(request.getEndLocationCode()));
            
            // Calcula tarifas
            calculateTariffs(request, response, tariffData);
            
        } catch (Exception e) {
            System.err.println("Erro ao calcular tarifa: " + e.getMessage());
            setDefaultValues(response, request.getTotalPrice());
        }
        
        return response;
    }

    private TariffCalculationResponse buildInitialResponse(TariffRequestDto request) {
        TariffCalculationResponse response = new TariffCalculationResponse();
        response.setHsCode(request.getHsCode());
        response.setTotalPrice(request.getTotalPrice());
        response.setStartLocationCode(request.getStartLocationCode());
        response.setEndLocationCode(request.getEndLocationCode());
        return response;
    }

    private void calculateTariffs(TariffRequestDto request, TariffCalculationResponse response, List<TariffData> tariffData) {
        List<TariffCalculationResponse.TariffDetail> tariffDetails = new ArrayList<>();
        double totalTariff = 0.0;
        
        for (TariffData data : tariffData) {
            if (data.getReportingEconomyCode().equals(request.getEndLocationCode())) {
                double tariffRate = data.getValue() != null ? data.getValue() : 0.0;
                double price = Double.parseDouble(request.getTotalPrice());
                double calculatedTariff = calculateTariffAmount(price, tariffRate);
                
                TariffCalculationResponse.TariffDetail detail = new TariffCalculationResponse.TariffDetail(
                    data.getReportingEconomyCode(),
                    data.getReportingEconomy(),
                    tariffRate,
                    calculatedTariff
                );
                
                tariffDetails.add(detail);
                totalTariff += calculatedTariff;
            }
        }
        
        response.setTariffDetails(tariffDetails);
        response.setTotalTariff(totalTariff);
        response.setFinalPrice(Double.parseDouble(request.getTotalPrice()) + totalTariff);
    }

    private double calculateTariffAmount(double price, double tariffRate) {
        return (price * tariffRate) / 100.0;
    }

    private void setDefaultValues(TariffCalculationResponse response, String totalPrice) {
        response.setTotalTariff(0.0);
        response.setFinalPrice(Double.parseDouble(totalPrice));
        response.setTariffDetails(new ArrayList<>());
    }
}