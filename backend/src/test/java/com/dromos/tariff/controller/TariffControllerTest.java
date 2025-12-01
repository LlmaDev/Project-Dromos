package com.dromos.tariff.controller;

import com.dromos.tariff.model.*;
import com.dromos.tariff.service.TariffService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TariffController.class)
@ActiveProfiles("test")
class TariffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TariffService tariffService;
     @Test
    void getCountries_ShouldReturnCountriesList() throws Exception {
        // Given
        List<Country> countries = Arrays.asList(
            new Country("076", "Brazil"),
            new Country("840", "United States of America")
        );
        when(tariffService.getCountries()).thenReturn(countries);

        // When & Then
        mockMvc.perform(get("/api/tariffs/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].code").value("076"))
                .andExpect(jsonPath("$[0].name").value("Brazil"))
                .andExpect(jsonPath("$[1].code").value("840"))
                .andExpect(jsonPath("$[1].name").value("United States of America"));

        verify(tariffService, times(1)).getCountries();
    }
    
    @Test
    void calculateTariff_ShouldReturnTariffCalculation() throws Exception {
        // Given
        TariffRequestDto request = new TariffRequestDto("97", "200", "950", "076");
        TariffCalculationResponse response = new TariffCalculationResponse();
        response.setHsCode("97");
        response.setTotalPrice("200");
        response.setStartLocationCode("950");
        response.setEndLocationCode("076");
        response.setStartLocationName("Africa");
        response.setEndLocationName("Brazil");
        response.setTotalTariff(8.0);
        response.setFinalPrice(208.0);

        when(tariffService.calculateTariff(any(TariffRequestDto.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/tariffs/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hsCode").value("97"))
                .andExpect(jsonPath("$.totalPrice").value("200"))
                .andExpect(jsonPath("$.startLocationCode").value("950"))
                .andExpect(jsonPath("$.endLocationCode").value("076"))
                .andExpect(jsonPath("$.startLocationName").value("Africa"))
                .andExpect(jsonPath("$.endLocationName").value("Brazil"))
                .andExpect(jsonPath("$.totalTariff").value(8.0))
                .andExpect(jsonPath("$.finalPrice").value(208.0));

        verify(tariffService, times(1)).calculateTariff(any(TariffRequestDto.class));
    }
}