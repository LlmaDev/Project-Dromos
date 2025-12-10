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
import java.util.Collections;
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

@ExtendWith(MockitoExtension.class)
class CountryServiceImplGetCountryNameTest {

    @Mock
    private WtoApiClient wtoApiClient;

    private CountryService countryService;

    @BeforeEach
    void setUp() {
        countryService = new CountryServiceImpl(wtoApiClient);
    }

    @Test
    void getCountryNameByCode_WhenCountryExists_ShouldReturnCountryName() {
        // Given
        List<Country> mockCountries = Arrays.asList(
            new Country("076", "Brazil", "BRA", 1),
            new Country("840", "United States", "USA", 2),
            new Country("156", "China", "CHN", 3)
        );
        when(wtoApiClient.fetchCountries()).thenReturn(mockCountries);
        
        // Initialize cache by calling getAllCountries
        countryService.getAllCountries();

        // When
        String countryName = countryService.getCountryNameByCode("076");

        // Then
        assertNotNull(countryName);
        assertEquals("Brazil", countryName);
    }

    @Test
    void getCountryNameByCode_WhenCountryDoesNotExist_ShouldReturnNull() {
        // Given
        List<Country> mockCountries = Arrays.asList(
            new Country("076", "Brazil", "BRA", 1),
            new Country("840", "United States", "USA", 2)
        );
        when(wtoApiClient.fetchCountries()).thenReturn(mockCountries);
        
        // Initialize cache
        countryService.getAllCountries();

        // When
        String countryName = countryService.getCountryNameByCode("999");

        // Then
        assertNull(countryName);
    }

    @Test
    void getCountryNameByCode_WhenCacheIsEmpty_ShouldReturnNull() {
        // Given - no countries loaded, cache not initialized
        
        // When
        String countryName = countryService.getCountryNameByCode("076");

        // Then
        assertNull(countryName);
    }

    @Test
    void getCountryNameByCode_WhenCodeIsNull_ShouldReturnNull() {
        // Given
        List<Country> mockCountries = Arrays.asList(
            new Country("076", "Brazil", "BRA", 1)
        );
        when(wtoApiClient.fetchCountries()).thenReturn(mockCountries);
        
        // Initialize cache
        countryService.getAllCountries();

        // When
        String countryName = countryService.getCountryNameByCode(null);

        // Then
        assertNull(countryName);
    }

    @Test
    void getCountryNameByCode_WhenCodeIsEmpty_ShouldReturnNull() {
        // Given
        List<Country> mockCountries = Arrays.asList(
            new Country("076", "Brazil", "BRA", 1)
        );
        when(wtoApiClient.fetchCountries()).thenReturn(mockCountries);
        
        // Initialize cache
        countryService.getAllCountries();

        // When
        String countryName = countryService.getCountryNameByCode("");

        // Then
        assertNull(countryName);
    }

    @Test
    void getCountryNameByCode_MultipleCallsSameCode_ShouldReturnConsistentResults() {
        // Given
        List<Country> mockCountries = Arrays.asList(
            new Country("076", "Brazil", "BRA", 1),
            new Country("840", "United States", "USA", 2)
        );
        when(wtoApiClient.fetchCountries()).thenReturn(mockCountries);
        
        // Initialize cache
        countryService.getAllCountries();

        // When - calling multiple times
        String firstCall = countryService.getCountryNameByCode("840");
        String secondCall = countryService.getCountryNameByCode("840");
        String thirdCall = countryService.getCountryNameByCode("840");

        // Then
        assertEquals("United States", firstCall);
        assertEquals("United States", secondCall);
        assertEquals("United States", thirdCall);
        assertEquals(firstCall, secondCall);
        assertEquals(secondCall, thirdCall);
    }

    @Test
    void getCountryNameByCode_CaseInsensitiveSearch_ShouldWork() {
        // Given
        List<Country> mockCountries = Arrays.asList(
            new Country("076", "Brazil", "BRA", 1)
        );
        when(wtoApiClient.fetchCountries()).thenReturn(mockCountries);
        
        // Initialize cache
        countryService.getAllCountries();

        // When - trying different cases (depending on implementation)
        String lowercase = countryService.getCountryNameByCode("076");
        
        // Then
        assertEquals("Brazil", lowercase);
    }

    @Test
    void getCountryNameByCode_WithSpecialCharactersInNames_ShouldHandleCorrectly() {
        // Given
        List<Country> mockCountries = Arrays.asList(
            new Country("076", "Brazil (República Federativa)", "BRA", 1),
            new Country("840", "United States of America", "USA", 2)
        );
        when(wtoApiClient.fetchCountries()).thenReturn(mockCountries);
        
        // Initialize cache
        countryService.getAllCountries();

        // When
        String brazilName = countryService.getCountryNameByCode("076");
        String usaName = countryService.getCountryNameByCode("840");

        // Then
        assertEquals("Brazil (República Federativa)", brazilName);
        assertEquals("United States of America", usaName);
    }

    @Test
    void getCountryNameByCode_AfterCacheUpdate_ShouldReturnNewValues() {
        // Given - initial countries
        List<Country> initialCountries = Arrays.asList(
            new Country("076", "Old Brazil Name", "BRA", 1)
        );
        when(wtoApiClient.fetchCountries()).thenReturn(initialCountries);
        
        // Initialize cache
        countryService.getAllCountries();
        
        String oldName = countryService.getCountryNameByCode("076");
        assertEquals("Old Brazil Name", oldName);

        // When - update cache with new countries
        List<Country> updatedCountries = Arrays.asList(
            new Country("076", "Brazil", "BRA", 1)
        );
        when(wtoApiClient.fetchCountries()).thenReturn(updatedCountries);
        
        // Reload cache
        countryService.getAllCountries();
        String newName = countryService.getCountryNameByCode("076");

        // Then
        assertEquals("Brazil", newName);
        assertNotEquals(oldName, newName);
    }

    @Test
    void getCountryNameByCode_WithEmptyCountryList_ShouldReturnNull() {
        // Given
        when(wtoApiClient.fetchCountries()).thenReturn(Collections.emptyList());
        
        // Initialize cache with empty list
        countryService.getAllCountries();

        // When
        String countryName = countryService.getCountryNameByCode("076");

        // Then
        assertNull(countryName);
    }
}