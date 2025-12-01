package com.dromos.tariff.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationConfigTest {

    @Test
    void restTemplate_ShouldBeConfigured() {
        // Given
        ApplicationConfig config = new ApplicationConfig();

        // When
        RestTemplate restTemplate = config.restTemplate();

        // Then
        assertNotNull(restTemplate);
    }

    @Test
    void objectMapper_ShouldBeConfigured() {
        // Given
        ApplicationConfig config = new ApplicationConfig();

        // When
        ObjectMapper objectMapper = config.objectMapper();

        // Then
        assertNotNull(objectMapper);
    }
}