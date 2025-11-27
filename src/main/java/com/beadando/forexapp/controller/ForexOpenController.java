package com.beadando.forexapp.controller;

import com.beadando.forexapp.service.ForexService;
import com.beadando.forexapp.service.Position;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.beadando.forexapp.service.PositionService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ForexOpenController {
    private final PositionService positionService;

    private final ForexService forexService;

    // memóriában tároljuk a pozíciókat


    public ForexOpenController(ForexService forexService,
                               PositionService positionService) {
        this.forexService = forexService;
        this.positionService = positionService;
    }

    @GetMapping("/nyit")
    public String openPosition(
            @RequestParam(required = false) String instrument,
            @RequestParam(required = false) Double amount,
            Model model
    ) {

        String[] instruments = {"EUR_USD", "GBP_USD", "USD_JPY", "EUR_HUF", "USD_HUF"};
        model.addAttribute("instruments", instruments);

        if (instrument != null && amount != null && amount != 0) {

            String side = amount > 0 ? "LONG" : "SHORT";
            double units = Math.abs(amount); // ne legyen negatív
            double price = forexService.getCurrentPrice(instrument);

            Position position = new Position(
                    instrument,
                    side,
                    units,
                    price
            );

            positionService.add(position);
            model.addAttribute("position", position);
        }

        return "nyit";
    }
}
