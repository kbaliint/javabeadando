package com.beadando.forexapp.controller;

import com.beadando.forexapp.service.ForexService;
import com.beadando.forexapp.service.PositionService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForexAccountController {

    private final PositionService positionService;
    private final ForexService forexService;

    public ForexAccountController(PositionService positionService, ForexService forexService) {
        this.positionService = positionService;
        this.forexService = forexService;
    }

    @GetMapping("/account")
    public String account(Model model) {

        double balance = 25000;

        double floating = positionService.calculateFloatingPL(forexService);

        model.addAttribute("balance", balance);
        model.addAttribute("currency", "USD");
        model.addAttribute("floating", floating);

        model.addAttribute("positions", positionService.getAll());

        return "account";
    }
}
