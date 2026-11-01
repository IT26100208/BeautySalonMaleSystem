package com.WD45.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.service.SalonServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/services")
public class CustomerServiceController {

    @Autowired
    private SalonServiceService salonServiceService;

    @GetMapping
    public String viewServices(Model model) {
        model.addAttribute("services", salonServiceService.getAllServices());
        return "customer/services-catalog";
    }

    @GetMapping("/{id}")
    public String serviceDetails(@PathVariable Long id, Model model) {
        model.addAttribute("service", salonServiceService.getServiceById(id));
        return "customer/service-details";
    }
}