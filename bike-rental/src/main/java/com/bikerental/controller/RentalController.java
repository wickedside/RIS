package com.bikerental.controller;

import com.bikerental.model.Bike;
import com.bikerental.model.Client;
import com.bikerental.model.Rental;
import com.bikerental.service.BikeService;
import com.bikerental.service.ClientService;
import com.bikerental.service.RentalService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RentalController {

    @Autowired
    private BikeService bikeService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private RentalService rentalService;

    @GetMapping("/rent/{id}")
    public String showRentalForm(@PathVariable("id") Long bikeId, Model model) {
        Bike bike = bikeService.getBikeById(bikeId);
        if (bike == null || !bike.isAvailable()) {
            return "redirect:/";
        }
        model.addAttribute("bike", bike);
        model.addAttribute("client", new Client());

        // Цены и комиссии
        Map<String, Double> prices = new HashMap<>();
        prices.put("1_hour", 10.0);
        prices.put("3_hours", 25.0);
        prices.put("1_day", 50.0);

        Map<String, Double> penalties = new HashMap<>();
        penalties.put("1_hour", 10.0);
        penalties.put("3_hours", 8.0);
        penalties.put("1_day", 50.0);

        model.addAttribute("prices", prices);
        model.addAttribute("penalties", penalties);

        return "rental_form";
    }

    @PostMapping("/rent")
    public String rentBike(@RequestParam("bikeId") Long bikeId,
                           @RequestParam("name") String name,
                           @RequestParam("email") String email,
                           @RequestParam("rentalType") String rentalType,
                           Model model) {

        Bike bike = bikeService.getBikeById(bikeId);
        if (bike == null || !bike.isAvailable()) {
            return "redirect:/";
        }

        Client client = new Client();
        client.setName(name);
        client.setEmail(email);
        clientService.saveClient(client);

        Rental rental = new Rental();
        rental.setBike(bike);
        rental.setClient(client);
        rental.setRentalType(rentalType);
        rental.setStartTime(LocalDateTime.now());
        rental.setTotalPrice(calculatePrice(rentalType));

        // Рассчитываем expectedEndTime и устанавливаем его
        LocalDateTime expectedEndTime = calculateExpectedEndTime(rental.getStartTime(), rentalType);
        rental.setExpectedEndTime(expectedEndTime);

        rentalService.saveRental(rental);

        bike.setAvailable(false);
        bikeService.saveBike(bike);

        // Форматируем даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedStartDate = rental.getStartTime().format(formatter);
        String formattedEndDate = rental.getExpectedEndTime().format(formatter);

        // Добавляем аренду и отформатированные даты в модель
        model.addAttribute("rental", rental);
        model.addAttribute("formattedStartDate", formattedStartDate);
        model.addAttribute("formattedEndDate", formattedEndDate);

        return "contract";
    }


    private Double calculatePrice(String rentalType) {
        switch (rentalType) {
            case "1_hour":
                return 10.0;
            case "3_hours":
                return 25.0;
            case "1_day":
                return 50.0;
            default:
                return 0.0;
        }
    }

    private LocalDateTime calculateEndTime(LocalDateTime startTime, String rentalType) {
        switch (rentalType) {
            case "1_hour":
                return startTime.plusHours(1);
            case "3_hours":
                return startTime.plusHours(3);
            case "1_day":
                return startTime.plusDays(1);
            default:
                return startTime;
        }
    }


    private String calculatePenaltyInfo(String rentalType) {
        switch (rentalType) {
            case "1_hour":
                return "10.0 руб. за каждый час просрочки.";
            case "3_hours":
                return "8.0 руб. за каждый час просрочки.";
            case "1_day":
                return "50.0 руб. за каждый день просрочки.";
            default:
                return "";
        }
    }

    @GetMapping("/contract/pdf/{rentalId}")
    public void generateContractPdf(@PathVariable("rentalId") Long rentalId, HttpServletResponse response) throws IOException {
        Rental rental = rentalService.getRentalById(rentalId);
        if (rental == null) {
            response.sendRedirect("/");
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=contract_" + rentalId + ".pdf");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Подключаем шрифт
            String fontPath = "fonts/arial.ttf";
            FontFactory.register(fontPath, "ArialUnicode");
            Font font = FontFactory.getFont("ArialUnicode", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String formattedStartDate = rental.getStartTime().format(formatter);
            String formattedEndDate = rental.getExpectedEndTime().format(formatter);

            document.add(new Paragraph("Договор аренды №" + rental.getId(), font));
            document.add(new Paragraph("Клиент: " + rental.getClient().getName(), font));
            document.add(new Paragraph("Email: " + rental.getClient().getEmail(), font));
            document.add(new Paragraph("Велосипед: " + rental.getBike().getModel(), font));
            document.add(new Paragraph("Тип аренды: " + rental.getRentalType(), font));
            document.add(new Paragraph("Стоимость: " + rental.getTotalPrice() + " руб.", font));
            document.add(new Paragraph("Дата начала аренды: " + formattedStartDate, font));
            document.add(new Paragraph("Дата окончания аренды: " + formattedEndDate, font));
            document.add(new Paragraph("Условия аренды:", font));
            document.add(new Paragraph("- Вы обязуетесь вернуть велосипед в установленный срок.", font));
            document.add(new Paragraph("- В случае просрочки будет начислена комиссия: " + calculatePenaltyInfo(rental.getRentalType()), font));
            document.add(new Paragraph("- Вы несете ответственность за повреждение велосипеда.", font));

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private Double calculatePenalty(Rental rental) {
        Duration duration = Duration.between(rental.getExpectedEndTime(), rental.getEndTime());
        long overdueHours = duration.toHours();

        Double penalty = 0.0;
        if (overdueHours > 0) {
            switch (rental.getRentalType()) {
                case "1_hour":
                    penalty = overdueHours * 10.0;
                    break;
                case "3_hours":
                    penalty = overdueHours * 8.0;
                    break;
                case "1_day":
                    penalty = (overdueHours / 24) * 50.0;
                    break;
                default:
                    penalty = 0.0;
            }
        }
        return penalty;
    }

    private LocalDateTime calculateExpectedEndTime(LocalDateTime startTime, String rentalType) {
        switch (rentalType) {
            case "1_hour":
                return startTime.plusHours(1);
            case "3_hours":
                return startTime.plusHours(3);
            case "1_day":
                return startTime.plusDays(1);
            default:
                return startTime;
        }
    }

    @GetMapping("/return/{rentalId}")
    public String returnBike(@PathVariable("rentalId") Long rentalId, Model model) {
        Rental rental = rentalService.getRentalById(rentalId);
        if (rental == null || rental.isReturned()) {
            return "redirect:/";
        }

        rental.setEndTime(LocalDateTime.now());
        rental.setReturned(true);

        // Рассчитать комиссию за просрочку
        Double penalty = calculatePenalty(rental);
        rental.setPenalty(penalty);

        rentalService.saveRental(rental);

        Bike bike = rental.getBike();
        bike.setAvailable(true);
        bikeService.saveBike(bike);

        model.addAttribute("rental", rental);
        return "return_confirmation";
    }


}
