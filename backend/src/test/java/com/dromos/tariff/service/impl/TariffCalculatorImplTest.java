package com.dromos.tariff.service.impl;

import com.dromos.tariff.model.*;
import com.dromos.tariff.service.CountryService;
import com.dromos.tariff.service.TariffCalculator;
import com.dromos.tariff.service.WtoApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TariffCalculatorImplTest {

    @Mock
    private WtoApiClient wtoApiClient;

    @Mock
    private CountryService countryService;

    private TariffCalculator tariffCalculator;

    @BeforeEach
    void setUp() {
        tariffCalculator = new TariffCalculatorImpl(wtoApiClient, countryService);
    }

    @Test
    void calculate_ShouldReturnCalculatedTariff() {
        // Given
        TariffRequestDto request = new TariffRequestDto("97", "200", "950", "076");
        
        TariffData tariffData = new TariffData();
        tariffData.setReportingEconomyCode("076");
        tariffData.setReportingEconomy("Brazil");
        tariffData.setValue(4.0);
        
        when(wtoApiClient.fetchTariffData("97", List.of("076"))).thenReturn(Arrays.asList(tariffData));
        when(countryService.getCountryNameByCode("950")).thenReturn("Africa");
        when(countryService.getCountryNameByCode("076")).thenReturn("Brazil");

        // When
        TariffCalculationResponse result = tariffCalculator.calculate(request);

        // Then
        assertNotNull(result);
        assertEquals("97", result.getHsCode());
        assertEquals("200", result.getTotalPrice());
        assertEquals("950", result.getStartLocationCode());
        assertEquals("076", result.getEndLocationCode());
        assertEquals("Africa", result.getStartLocationName());
        assertEquals("Brazil", result.getEndLocationName());
        assertEquals(8.0, result.getTotalTariff()); // 200 * 4% = 8
        assertEquals(208.0, result.getFinalPrice()); // 200 + 8 = 208
        assertEquals(1, result.getTariffDetails().size());
        assertEquals("076", result.getTariffDetails().get(0).getCountryCode());
        assertEquals("Brazil", result.getTariffDetails().get(0).getCountryName());
        assertEquals(4.0, result.getTariffDetails().get(0).getTariffRate());
        assertEquals(8.0, result.getTariffDetails().get(0).getCalculatedTariff());
    }

    @Test
    void calculate_WhenNoMatchingCountry_ShouldReturnZeroTariff() {
        // Given
        TariffRequestDto request = new TariffRequestDto("97", "200", "950", "076");
        
        TariffData tariffData = new TariffData();
        tariffData.setReportingEconomyCode("840"); // Diferente do endLocationCode
        tariffData.setValue(4.0);
        
        when(wtoApiClient.fetchTariffData("97", List.of("076"))).thenReturn(Arrays.asList(tariffData));
        when(countryService.getCountryNameByCode("950")).thenReturn("Africa");
        when(countryService.getCountryNameByCode("076")).thenReturn("Brazil");

        // When
        TariffCalculationResponse result = tariffCalculator.calculate(request);

        // Then
        assertNotNull(result);
        assertEquals(0.0, result.getTotalTariff());
        assertEquals(200.0, result.getFinalPrice());
        assertTrue(result.getTariffDetails().isEmpty());
    }

    @Test
    void calculate_WhenApiThrowsException_ShouldReturnDefaultValues() {
        // Given
        TariffRequestDto request = new TariffRequestDto("97", "200", "950", "076");
        
        when(wtoApiClient.fetchTariffData("97", List.of("076"))).thenThrow(new RuntimeException("API Error"));

        // When
        TariffCalculationResponse result = tariffCalculator.calculate(request);

        // Then
        assertNotNull(result);
        assertEquals("97", result.getHsCode());
        assertEquals("200", result.getTotalPrice());
        assertEquals(0.0, result.getTotalTariff());
        assertEquals(200.0, result.getFinalPrice());
        assertTrue(result.getTariffDetails().isEmpty());
    }

     @Test
    void calculate_WhenTariffValueIsNull_ShouldUseZero() {
        // Given
        TariffRequestDto request = new TariffRequestDto("97", "200", "950", "076");
        
        TariffData tariffData = new TariffData();
        tariffData.setReportingEconomyCode("076");
        tariffData.setReportingEconomy("Brazil");
        tariffData.setValue(null); // Valor nulo
        
        when(wtoApiClient.fetchTariffData("97", List.of("076"))).thenReturn(Arrays.asList(tariffData));
        when(countryService.getCountryNameByCode("950")).thenReturn("Africa");
        when(countryService.getCountryNameByCode("076")).thenReturn("Brazil");

        // When
        TariffCalculationResponse result = tariffCalculator.calculate(request);

        // Then
        assertNotNull(result);
        assertEquals(0.0, result.getTotalTariff());
        assertEquals(200.0, result.getFinalPrice());
        assertEquals(1, result.getTariffDetails().size());
        assertEquals(0.0, result.getTariffDetails().get(0).getTariffRate());
        assertEquals(0.0, result.getTariffDetails().get(0).getCalculatedTariff());
    }
}