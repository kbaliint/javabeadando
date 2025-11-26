package com.beadando.forexapp.controller;

import com.beadando.forexapp.service.MnbSoapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final MnbSoapService mnbService;

    public HomeController(MnbSoapService mnbService) {
        this.mnbService = mnbService;
    }

    // --- FŐOLDAL ---
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // --- SOAP ARFOLYAMOK ---
    @GetMapping("/mnb")
    public String soapPage(Model model) {

        String currenciesXml = mnbService.getCurrencies();
        String ratesXml = mnbService.getCurrentExchangeRates();

        model.addAttribute("currencies", mnbService.parseCurrencies(currenciesXml));
        model.addAttribute("rates", mnbService.parseRates(ratesXml));

        return "soap";
    }
}
