package com.dromos.tariff;

import com.dromos.tariff.controller.TariffController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TariffApplicationTests {

    @Autowired
    private TariffController controller;

    @Test
    void contextLoads() {
        // Verifica se o controller foi criado e injetado corretamente no contexto
        assertThat(controller).isNotNull();
    }
}