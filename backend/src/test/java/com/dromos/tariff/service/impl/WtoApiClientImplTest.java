package com.dromos.tariff.service.impl;

import com.dromos.tariff.model.Country;
import com.dromos.tariff.model.TariffData;
import com.dromos.tariff.model.TariffDataResponse;
import com.dromos.tariff.service.WtoApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WtoApiClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private WtoApiClient wtoApiClient;

    @BeforeEach
    void setUp() {
        wtoApiClient = new WtoApiClientImpl(restTemplate, objectMapper);
        // Usar reflexão para definir valores das propriedades @Value para teste
        setField("wtoApiKey", "test-key");
        setField("wtoBaseUrl", "https://test-api.wto.org/timeseries/v1");
    }
    
    @Test
    void fetchCountries_ShouldReturnCountriesList() throws Exception {
        // Given
        String jsonResponse = "[{\"code\":\"076\",\"name\":\"Brazil\"}]";
        List<Country> expectedCountries = Arrays.asList(new Country("076", "Brazil"));
        
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(eq(jsonResponse), any(TypeReference.class))).thenReturn(expectedCountries);

        // When
        List<Country> result = wtoApiClient.fetchCountries();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("076", result.get(0).getCode());
        assertEquals("Brazil", result.get(0).getName());
    }

    private void setField(String fieldName, String value) {
        try {
            var field = WtoApiClientImpl.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(wtoApiClient, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}