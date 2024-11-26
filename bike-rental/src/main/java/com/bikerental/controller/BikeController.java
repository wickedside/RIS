package com.bikerental.controller;

import com.bikerental.model.Bike;
import com.bikerental.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class BikeController {

    @Autowired
    private BikeService bikeService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        List<Bike> availableBikes = bikeService.getAvailableBikes();
        model.addAttribute("bikes", availableBikes);
        return "bikes";
    }

    // Дополнительные методы для управления велосипедами (для администратора)
}
