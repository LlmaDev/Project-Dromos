package com.dromos.tariff.service.impl;

import com.dromos.tariff.model.Country;
import com.dromos.tariff.service.CountryService;
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
class CountryServiceImplTest {

    @Mock
    private WtoApiClient wtoApiClient;

    private CountryService countryService;

    @BeforeEach
    void setUp() {
        countryService = new CountryServiceImpl(wtoApiClient);
    }

    @Test
    void getAllCountries_ShouldReturnFilteredCountriesList() {
        // Given
        List<Country> mockCountries = Arrays.asList(
            new Country("076", "Brazil", "BRA", 1),
            new Country("840", "United States", "USA", 2)
        );
        when(wtoApiClient.fetchCountries()).thenReturn(mockCountries);

        // When
        List<Country> result = countryService.getAllCountries();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("076", result.get(0).getCode());
        assertEquals("Brazil", result.get(0).getName());
        assertNull(result.get(0).getIso3A()); // Deve ser null pois só retorna code e name
        assertNull(result.get(0).getDisplayOrder()); // Deve ser null pois só retorna code e name
    }

}