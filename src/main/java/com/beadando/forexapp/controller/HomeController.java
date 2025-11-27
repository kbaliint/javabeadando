package com.beadando.forexapp.controller;

import com.beadando.forexapp.service.MnbSoapService;
import com.beadando.forexapp.service.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

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

    // --- SOAP MENÜ / GRAFIKON ---
    @GetMapping("/soap")
    public String soapPage(
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model
    ) {
        // valuta lista mindig kell
        String currenciesXml = mnbService.getCurrencies();
        model.addAttribute("currencies", mnbService.parseCurrencies(currenciesXml));

        // csak akkor kérünk történeti adatot, ha minden paraméter megvan
        if (currency != null && !currency.isBlank()
                && startDate != null && !startDate.isBlank()
                && endDate != null && !endDate.isBlank()) {

            System.out.println("SOAP REQUEST -> " + currency + ", " + startDate + " -> " + endDate);

            String xml = mnbService.getRatesByDate(startDate, endDate, currency);

            System.out.println("SOAP XML RESPONSE:");
            System.out.println(xml);

            Map<String, Double> rates = mnbService.parseRatesFromXml(xml);


            model.addAttribute("chartLabels", rates.keySet());
            model.addAttribute("chartValues", rates.values());
        }

        return "soap";
    }

    @GetMapping("/account")
    public String accountPage(Model model) {

        model.addAttribute("owner", "Bálint Forex Account");
        model.addAttribute("accountNumber", "HU42-1234-5678-9999");
        model.addAttribute("balance", 1542300);
        model.addAttribute("currency", "HUF");

        List<Transaction> transactions = List.of(
                new Transaction("2025-11-20", "VÉTEL", "EUR", 200, 1500000),
                new Transaction("2025-11-21", "ELADÁS", "USD", -100, 1530000),
                new Transaction("2025-11-22", "VÉTEL", "GBP", 50, 1542300)
        );

        model.addAttribute("transactions", transactions);

        return "account";
    }


}
