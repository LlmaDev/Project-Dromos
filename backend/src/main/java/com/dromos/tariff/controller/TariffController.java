package com.dromos.tariff.controller;

import com.dromos.tariff.model.Tariff;
import com.dromos.tariff.service.TariffService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tariffs")
public class TariffController {
    private final TariffService service;

    public TariffController(TariffService service) {
        this.service = service;
    }

    @GetMapping
    public Tariff getTariff(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String hsCode) {
        return service.getTariff(origin, destination, hsCode);
    }
}
