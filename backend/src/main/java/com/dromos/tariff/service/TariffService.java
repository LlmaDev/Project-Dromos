package com.dromos.tariff.service;

import com.dromos.tariff.model.Tariff;
import org.springframework.stereotype.Service;

@Service
public class TariffService {

    public Tariff getTariff(String origin, String destination, String hsCode) {
        // Simula um fetch de API externa
        return new Tariff(origin, destination, hsCode, 12.5, "%");
    }
}
