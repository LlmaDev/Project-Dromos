package com.dromos.tariff.controller;

import com.dromos.tariff.model.Tariff;
import com.dromos.tariff.service.TariffService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TariffController.class)
class TariffControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simula as requisições HTTP

    @MockBean
    private TariffService service; // Cria uma versão mockada do Service

    @Test
    @DisplayName("GET /api/tariffs - Deve retornar status 200 e JSON correto")
    void shouldReturnTariffData() throws Exception {
        // 1. Define o comportamento do Mock
        when(service.getTariff("BR", "US", "1234"))
                .thenReturn(new Tariff("BR", "US", "1234", 12.5, "%"));

        // 2. Faz a requisição simulada e verifica os resultados
        mockMvc.perform(get("/api/tariffs")
                        .param("origin", "BR")
                        .param("destination", "US")
                        .param("hsCode", "1234"))
                .andExpect(status().isOk()) // Espera HTTP 200 (ok)
                .andExpect(jsonPath("$.origin").value("BR"))
                .andExpect(jsonPath("$.rate").value(12.5));

    }

    @Test
    @DisplayName("GET /api/tariffs - Deve retornar 400 Bad Request se faltar parâmetro")
    void shouldReturn400WhenMissingParam() throws Exception {
        // Tenta chamar sem passar o "destination"
        mockMvc.perform(get("/api/tariffs")
                        .param("origin", "BR")
                        .param("hsCode", "1234"))
                .andExpect(status().isBadRequest()); // Espera erro 400 (not found)
    }

}

