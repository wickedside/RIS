package com.bikerental.controller;

import com.bikerental.model.Bike;
import com.bikerental.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BikeService bikeService;

    @GetMapping("/bikes")
    public String listBikes(Model model) {
        model.addAttribute("bikes", bikeService.getAllBikes());
        return "admin_bikes";
    }

    @GetMapping("/bikes/new")
    public String showAddBikeForm(Model model) {
        model.addAttribute("bike", new Bike());
        return "add_bike";
    }

    @PostMapping("/bikes")
    public String addBike(@ModelAttribute("bike") Bike bike) {
        bikeService.saveBike(bike);
        return "redirect:/admin/bikes";
    }

    @GetMapping("/bikes/edit/{id}")
    public String showEditBikeForm(@PathVariable("id") Long id, Model model) {
        Bike bike = bikeService.getBikeById(id);
        if (bike == null) {
            return "redirect:/admin/bikes";
        }
        model.addAttribute("bike", bike);
        return "edit_bike";
    }

    @PostMapping("/bikes/{id}")
    public String updateBike(@PathVariable("id") Long id, @ModelAttribute("bike") Bike bike) {
        bike.setId(id);
        bikeService.saveBike(bike);
        return "redirect:/admin/bikes";
    }

    @GetMapping("/bikes/delete/{id}")
    public String deleteBike(@PathVariable("id") Long id) {
        bikeService.deleteBike(id);
        return "redirect:/admin/bikes";
    }
}