package com.beadando.forexapp.controller;

import com.beadando.forexapp.service.PositionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForexPositionController {

    private final PositionService positionService;

    public ForexPositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping("/poz")
    public String positionsPage(Model model) {
        model.addAttribute("positions", positionService.getAll());
        return "poz";
    }
}
