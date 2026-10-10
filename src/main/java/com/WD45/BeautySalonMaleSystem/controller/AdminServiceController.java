package com.WD45.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.entity.SalonService;
import com.WD45.BeautySalonMaleSystem.service.SalonServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/services")
public class AdminServiceController {

    @Autowired
    private SalonServiceService salonServiceService;

    @GetMapping
    public String listServices(Model model) {
        model.addAttribute("services", salonServiceService.getAllServices());
        return "admin/services-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("service", new SalonService());
        return "admin/service-add";
    }

    @PostMapping("/save")
    public String saveService(@ModelAttribute("service") SalonService service) {
        salonServiceService.saveService(service);
        return "redirect:/admin/services";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("service", salonServiceService.getServiceById(id));
        return "admin/service-edit";
    }

    @PostMapping("/update/{id}")
    public String updateService(@PathVariable Long id, @ModelAttribute("service") SalonService service) {
        salonServiceService.updateService(id, service);
        return "redirect:/admin/services";
    }

    @GetMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id) {
        salonServiceService.deleteService(id);
        return "redirect:/admin/services";
    }
}