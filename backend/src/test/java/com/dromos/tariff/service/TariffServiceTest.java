package com.dromos.tariff.service;

import com.dromos.tariff.model.Tariff;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TariffServiceTest {

    @Test
    @DisplayName("Deve retornar a tarifa fixa simulada corretamente")
    void shouldReturnFixedTariff() {
        TariffService service = new TariffService();
        String origin = "BR";
        String destination = "US";
        String hsCode = "1001.99";

        Tariff result = service.getTariff(origin, destination, hsCode);

        assertNotNull(result, "O objeto Tariff não deveria ser nulo");
        assertEquals(origin, result.getOrigin());
        assertEquals(destination, result.getDestination());
        assertEquals(hsCode, result.getHsCode());
        assertEquals(12.5, result.getRate(), "A taxa fixa deve ser 12.5");
        assertEquals("%", result.getCurrency());
    }
}