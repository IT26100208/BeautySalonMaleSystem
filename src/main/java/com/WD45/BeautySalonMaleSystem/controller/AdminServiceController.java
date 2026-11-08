package com.WD45.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.dto.ServiceDTO;
import com.WD45.BeautySalonMaleSystem.dto.ServiceFormDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.WD45.BeautySalonMaleSystem.service.SalonServiceService;
import java.util.List;

@Controller
@RequestMapping("/dashboard/services")
public class AdminServiceController {

    private final SalonServiceService salonServiceService;

    public AdminServiceController(SalonServiceService salonServiceService) {
        this.salonServiceService = salonServiceService;
    }

    @GetMapping
    public String viewServices(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            Model model) {

        List<ServiceDTO> services;

        if (keyword != null && !keyword.trim().isEmpty()) {
            services = salonServiceService.searchAllServices(keyword.trim());
        } else if (category != null
                && !category.trim().isEmpty()
                && !"All".equalsIgnoreCase(category.trim())) {
            services = salonServiceService.getAllServicesByCategory(category.trim());
        } else {
            services = salonServiceService.getAllServices();
        }

        List<ServiceDTO> allServices = salonServiceService.getAllServices();
        long activeCount = allServices.stream()
                .filter(service -> Boolean.TRUE.equals(service.getIsActive()))
                .count();

        model.addAttribute("services", services);
        model.addAttribute("categories", salonServiceService.getAllCategories());
        model.addAttribute("totalServices", allServices.size());
        model.addAttribute("activeServices", activeCount);
        model.addAttribute("inactiveServices", allServices.size() - activeCount);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("selectedCategory", category != null ? category : "All");

        if (!model.containsAttribute("serviceForm")) {
            model.addAttribute("serviceForm", new ServiceFormDTO());
        }

        return "admin/services-list";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ServiceDTO> getServiceDetails(@PathVariable Long id) {
        return ResponseEntity.ok(salonServiceService.getServiceById(id));
    }

    @PostMapping("/add")
    public String addService(
            @Valid @ModelAttribute("serviceForm") ServiceFormDTO formDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            String message = result.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute(
                    "error",
                    message != null ? message : "Please provide valid service details.");
            redirectAttributes.addFlashAttribute("serviceForm", formDTO);
            return "redirect:/dashboard/services";
        }

        try {
            salonServiceService.createService(formDTO);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Service '" + formDTO.getName() + "' created successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("serviceForm", formDTO);
        }

        return "redirect:/dashboard/services";
    }

    @PostMapping("/edit/{id}")
    public String editService(
            @PathVariable Long id,
            @Valid @ModelAttribute("serviceForm") ServiceFormDTO formDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            String message = result.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute(
                    "error",
                    message != null ? message : "Please correct the form inputs.");
            return "redirect:/dashboard/services";
        }

        try {
            salonServiceService.updateService(id, formDTO);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Service '" + formDTO.getName() + "' updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/dashboard/services";
    }

    @PostMapping("/delete/{id}")
    public String deleteService(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            salonServiceService.deleteService(id);
            redirectAttributes.addFlashAttribute("success", "Service deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Unable to delete service.");
        }

        return "redirect:/dashboard/services";
    }

    @PostMapping("/toggle/{id}")
    public String toggleStatus(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            salonServiceService.toggleServiceStatus(id);
            redirectAttributes.addFlashAttribute("success", "Service status updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Unable to update service status.");
        }

        return "redirect:/dashboard/services";
    }
}