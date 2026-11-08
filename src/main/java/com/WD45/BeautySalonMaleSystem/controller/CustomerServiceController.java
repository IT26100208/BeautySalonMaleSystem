package com.WD45.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.dto.ServiceDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.WD45.BeautySalonMaleSystem.service.SalonServiceService;

import java.util.List;

@Controller
public class CustomerServiceController {

    private final SalonServiceService salonServiceService;

    public CustomerServiceController(SalonServiceService salonServiceService) {
        this.salonServiceService = salonServiceService;
    }

    @GetMapping({"/services", "/customer/services"})
    public String viewServicesCatalog(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        List<ServiceDTO> services;

        if (keyword != null && !keyword.trim().isEmpty()) {
            services = salonServiceService.searchActiveServices(keyword.trim());
        } else if (category != null
                && !category.trim().isEmpty()
                && !"All".equalsIgnoreCase(category.trim())) {
            services = salonServiceService.getServicesByCategory(category.trim());
        } else {
            services = salonServiceService.getAllActiveServices();
        }

        model.addAttribute("services", services);
        model.addAttribute("categories", salonServiceService.getAllCategories());
        model.addAttribute("selectedCategory", category != null ? category : "All");
        model.addAttribute("keyword", keyword != null ? keyword : "");

        return "customer/services";
    }

    @GetMapping("/services/{id}")
    public String serviceDetails(@PathVariable Long id, Model model) {
        model.addAttribute("service", salonServiceService.getServiceById(id));
        return "customer/service-details";
    }
}