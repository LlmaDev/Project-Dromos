package com.dromos.tariff.service;

import com.dromos.tariff.model.*;
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
class TariffServiceTest {

    @Mock
    private CountryService countryService;

    @Mock
    private TariffCalculator tariffCalculator;

    private TariffService tariffService;

    @BeforeEach
    void setUp() {
        tariffService = new TariffService(countryService, tariffCalculator);
    }

    @Test
    void getCountries_ShouldDelegateToCountryService() {
        // Given
        List<Country> expectedCountries = Arrays.asList(
            new Country("076", "Brazil"),
            new Country("840", "United States")
        );
        when(countryService.getAllCountries()).thenReturn(expectedCountries);

        // When
        List<Country> result = tariffService.getCountries();

        // Then
        assertEquals(expectedCountries, result);
        verify(countryService, times(1)).getAllCountries();
    }

    @Test
    void calculateTariff_ShouldDelegateToTariffCalculator() {
        // Given
        TariffRequestDto request = new TariffRequestDto("97", "200", "950", "076");
        TariffCalculationResponse expectedResponse = new TariffCalculationResponse();
        expectedResponse.setHsCode("97");
        expectedResponse.setTotalTariff(8.0);
        
        when(tariffCalculator.calculate(request)).thenReturn(expectedResponse);

        // When
        TariffCalculationResponse result = tariffService.calculateTariff(request);

        // Then
        assertEquals(expectedResponse, result);
        verify(tariffCalculator, times(1)).calculate(request);
    }

}
