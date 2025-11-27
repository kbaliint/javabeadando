package com.beadando.forexapp.controller;

import com.beadando.forexapp.service.MnbSoapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ForexHistoryController {

    private final MnbSoapService mnbService;

    public ForexHistoryController(MnbSoapService mnbService) {
        this.mnbService = mnbService;
    }

    @GetMapping("/hist")
    public String historyPage(
            @RequestParam(required = false) String instrument,
            @RequestParam(required = false) String granularity,
            Model model
    ) {
        String[] instruments = {"EUR_HUF", "USD_HUF", "GBP_HUF"};
        model.addAttribute("instruments", instruments);

        String[] granularities = {"D", "H1", "M15"};
        model.addAttribute("granularities", granularities);

        if (instrument != null && granularity != null) {

            String currency = instrument.split("_")[0];

            // utolsó 15 nap → majd csak 10 jelenik meg
            String xml = mnbService.getLastNDays(currency, 15);

            Map<String, Double> rates = mnbService.parseRatesFromXml(xml);

            model.addAttribute("history", rates);
            model.addAttribute("instrument", instrument);
            model.addAttribute("granularity", granularity);
        }

        return "hist";
    }
}
