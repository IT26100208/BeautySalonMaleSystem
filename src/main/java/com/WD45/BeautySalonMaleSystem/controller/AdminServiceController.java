package com.WD45.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.dto.ServiceDTO;
import com.WD45.BeautySalonMaleSystem.dto.ServiceFormDTO;
import com.WD45.BeautySalonMaleSystem.service.SalonServiceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/dashboard/services")
public class AdminServiceController {

    private final SalonServiceService salonServiceService;

    public AdminServiceController(SalonServiceService salonServiceService) {
        this.salonServiceService = salonServiceService;
    }

    // LIST SERVICES IN ADMIN DASHBOARD
    @GetMapping
    public String viewServices(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            Model model) {

        List<ServiceDTO> services;

        if (keyword != null && !keyword.trim().isEmpty()) {
            services = salonServiceService.searchAllServices(keyword);
        } else if (category != null && !category.trim().isEmpty() && !"All".equalsIgnoreCase(category)) {
            services = salonServiceService.getServicesByCategory(category);
        } else {
            services = salonServiceService.getAllServices();
        }

        List<ServiceDTO> allServices = salonServiceService.getAllServices();
        long activeCount = allServices.stream().filter(s -> Boolean.TRUE.equals(s.getIsActive())).count();

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

        return "admin/services";
    }

    // GET SERVICE DETAILS BY ID (FOR EDIT/DETAILS MODAL AJAX)
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ServiceDTO> getServiceDetails(@PathVariable Long id) {
        try {
            ServiceDTO service = salonServiceService.getServiceById(id);
            return ResponseEntity.ok(service);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ADD NEW SERVICE
    @PostMapping("/add")
    public String addService(
            @Valid @ModelAttribute("serviceForm") ServiceFormDTO formDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            String firstError = result.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", firstError != null ? firstError : "Please provide valid service details.");
            redirectAttributes.addFlashAttribute("serviceForm", formDTO);
            return "redirect:/dashboard/services";
        }

        try {
            salonServiceService.createService(formDTO);
            redirectAttributes.addFlashAttribute("success", "Service '" + formDTO.getName() + "' created successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("serviceForm", formDTO);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create service: " + e.getMessage());
        }

        return "redirect:/dashboard/services";
    }

    // EDIT / UPDATE SERVICE
    @PostMapping("/edit/{id}")
    public String editService(
            @PathVariable Long id,
            @Valid @ModelAttribute("serviceForm") ServiceFormDTO formDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            String firstError = result.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", firstError != null ? firstError : "Please correct form inputs.");
            return "redirect:/dashboard/services";
        }

        try {
            salonServiceService.updateService(id, formDTO);
            redirectAttributes.addFlashAttribute("success", "Service '" + formDTO.getName() + "' updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unable to update service: " + e.getMessage());
        }

        return "redirect:/dashboard/services";
    }

    // DELETE SERVICE
    @PostMapping("/delete/{id}")
    public String deleteService(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            salonServiceService.deleteService(id);
            redirectAttributes.addFlashAttribute("success", "Service deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unable to delete service: " + e.getMessage());
        }

        return "redirect:/dashboard/services";
    }

    // TOGGLE ACTIVE / INACTIVE STATUS
    @PostMapping("/toggle/{id}")
    public String toggleStatus(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            salonServiceService.toggleServiceStatus(id);
            redirectAttributes.addFlashAttribute("success", "Service status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unable to update service status.");
        }

        return "redirect:/dashboard/services";
    }
}
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
        return "admin/services-list"; // Renders src/main/resources/templates/admin/services-list.html
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("service", new SalonService());
        return "admin/service-add"; // Renders src/main/resources/templates/admin/service-add.html
    }

    @PostMapping("/save")
    public String saveService(@ModelAttribute("service") SalonService service) {
        salonServiceService.saveService(service);
        return "redirect:/admin/services";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("service", salonServiceService.getServiceById(id));
        return "admin/service-edit"; // Renders src/main/resources/templates/admin/service-edit.html
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
