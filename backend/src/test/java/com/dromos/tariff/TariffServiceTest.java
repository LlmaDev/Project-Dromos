package com.dromos.tariff;

import com.dromos.tariff.model.Tariff;
import com.dromos.tariff.service.TariffService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TariffServiceTest {

    @Test
    void shouldReturnTariff() {
        TariffService service = new TariffService();
        Tariff t = service.getTariff("BR", "CN", "1001");
        assertEquals("BR", t.getOrigin());
        assertEquals("CN", t.getDestination());
    }
}
