package com.beadando.forexapp.controller;

import com.beadando.forexapp.service.ForexService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AktualController {

    private final ForexService forexService;

    public AktualController(ForexService forexService) {
        this.forexService = forexService;
    }

    @GetMapping("/aktual")
    public String aktualPage(
            @RequestParam(required = false) String instrument,
            Model model
    ) {
        System.out.println("AKTUAL PAGE MEGHÍVVA, instrument = " + instrument);
        // instrumentum-lista
        String[] instruments = {"EUR_USD", "GBP_USD", "USD_JPY", "EUR_HUF", "USD_HUF"};
        model.addAttribute("instruments", instruments);

        if (instrument != null) {

            Double price = forexService.getCurrentPrice(instrument);
            Double prevPrice = forexService.getPreviousPrice(instrument); // ezt írjuk meg most

            String trend = "flat";
            if (price != null && prevPrice != null) {
                if (price > prevPrice) trend = "up";
                if (price < prevPrice) trend = "down";
            }

            model.addAttribute("instrument", instrument);
            model.addAttribute("price", price);
            model.addAttribute("trend", trend);
        }

        return "aktual";
    }

}
