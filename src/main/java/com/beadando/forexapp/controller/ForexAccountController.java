package com.beadando.forexapp.controller;

import com.beadando.forexapp.service.Position;
import com.beadando.forexapp.service.Trade;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ForexAccountController {

    @GetMapping("/account")
    public String account(Model model) {

        // ----- TESZT ADATOK -----
        model.addAttribute("balance", 25000);
        model.addAttribute("currency", "USD");

        model.addAttribute("positions", List.of(
                new Position("EUR_USD", "BUY", 1.5, 1.0834),
                new Position("GBP_USD", "SELL", 0.8, 1.2521)
        ));

        model.addAttribute("trades", List.of(
                new Trade("EUR_USD", "BUY", "2025-11-20", 1.0810),
                new Trade("GBP_USD", "SELL", "2025-11-21", 1.2550)
        ));

        return "account";
    }
}
