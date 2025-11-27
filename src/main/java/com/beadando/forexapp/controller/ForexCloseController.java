package com.beadando.forexapp.controller;

import com.beadando.forexapp.service.ForexService;
import com.beadando.forexapp.service.Position;
import com.beadando.forexapp.service.PositionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForexCloseController {

    private final PositionService positionService;
    private final ForexService forexService;

    public ForexCloseController(PositionService positionService,
                                ForexService forexService) {
        this.positionService = positionService;
        this.forexService = forexService;
    }

    @GetMapping("/zar")
    public String closePage(
            @RequestParam(required = false) Long tradeId,
            Model model
    ) {

        model.addAttribute("positions", positionService.getAll());

        if (tradeId != null) {

            Position position = positionService.findById(tradeId);

            if (position != null) {

                double closePrice = forexService.getCurrentPrice(position.getInstrument());

                double pl = position.getSide().equals("LONG")
                        ? (closePrice - position.getOpenPrice()) * position.getUnits()
                        : (position.getOpenPrice() - closePrice) * position.getUnits();

                positionService.remove(position);

                model.addAttribute("closedPosition", position);
                model.addAttribute("closePrice", closePrice);
                model.addAttribute("pl", pl);
            }
        }

        return "zar";
    }
}
