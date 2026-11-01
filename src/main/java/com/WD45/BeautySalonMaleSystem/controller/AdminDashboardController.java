package com.WD45.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.dto.DashboardStatsDTO;
import com.WD45.BeautySalonMaleSystem.dto.UserFormDTO;
import com.WD45.BeautySalonMaleSystem.entity.SystemSettings;
import com.WD45.BeautySalonMaleSystem.service.DashboardService;
import com.WD45.BeautySalonMaleSystem.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    private final DashboardService dashboardService;
    private final UserService userService;

    @Autowired
    public AdminDashboardController(DashboardService dashboardService, UserService userService) {
        this.dashboardService = dashboardService;
        this.userService = userService;
    }

    // ADMIN DASHBOARD
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        DashboardStatsDTO stats = dashboardService.getDashboardStats();
        model.addAttribute("stats", stats);
        model.addAttribute("users", userService.getAllUsers());

        return "admin/dashboard";
    }

    //SETTINGS
    @GetMapping("/settings")
    public String showSettings(Model model) {
        SystemSettings settings = dashboardService.getSystemSettings();
        model.addAttribute("settings", settings);
        return "admin/settings";
    }

    @PostMapping("/settings")
    public String updateSettings(@ModelAttribute("settings") SystemSettings settings, Model model) {
        dashboardService.saveSystemSettings(settings);
        model.addAttribute("settings", dashboardService.getSystemSettings());
        model.addAttribute("successMessage", "Salon settings updated successfully!");
        return "admin/settings";
    }

    // UPDATE USER
    @PostMapping("/update/{id}")
    public String updateUser(

            @PathVariable Long id,

            @Valid
            @ModelAttribute("user")
            UserFormDTO user,

            BindingResult result,

            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Please enter valid user information."
            );

            return "redirect:/dashboard";
        }

        try {

            userService.updateUser(
                    id,
                    user
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "User updated successfully!"
            );

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/dashboard";
    }



    // DELETE USER


    @PostMapping("/delete/{id}")
    public String deleteUser(

            @PathVariable Long id,

            RedirectAttributes redirectAttributes) {

        try {

            userService.deleteUser(id);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "User deleted successfully!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Unable to delete user."
            );
        }

        return "redirect:/dashboard";
    }
}