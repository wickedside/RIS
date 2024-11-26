package com.bikerental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @GetMapping("/payback")
    public String showPaybackForm() {
        return "payback_form";
    }

    @PostMapping("/payback")
    public String calculatePayback(@RequestParam("bikeCost") Double bikeCost,
                                   @RequestParam("averageIncome") Double averageIncome,
                                   @RequestParam("maintenanceCost") Double maintenanceCost,
                                   Model model) {

        Double netIncome = averageIncome - maintenanceCost;
        Double paybackPeriod = bikeCost / netIncome;

        model.addAttribute("paybackPeriod", paybackPeriod);
        return "payback_result";
    }
}