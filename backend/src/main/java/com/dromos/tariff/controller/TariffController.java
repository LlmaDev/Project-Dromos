package com.dromos.tariff.controller;

import com.dromos.tariff.model.*;
import com.dromos.tariff.service.TariffService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
public class TariffController {
    private final TariffService service;

    public TariffController(TariffService service) {
        this.service = service;
    }

    /**
     * Busca lista de países da API WTO
     */
    @GetMapping("/countries")
    public List<Country> getCountries() {
        return service.getCountries();
    }
}
