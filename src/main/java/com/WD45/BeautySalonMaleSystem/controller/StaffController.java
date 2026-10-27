package com.WD45.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.entity.Staff;
import com.WD45.BeautySalonMaleSystem.service.StaffService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public String listStaff(Model model) {
        model.addAttribute(
                "staffList", staffService.getAllStaff()
        );
        return "staff/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("staff", new Staff());
        return "staff/form";
    }

    @PostMapping("/save")
    public String saveStaff(
            @ModelAttribute Staff staff,
            RedirectAttributes redirectAttributes) {

        staffService.saveStaff(staff);
        redirectAttributes.addFlashAttribute(
                "message", "Staff saved successfully!"
        );

        return "redirect:/admin/staff";
    }

    @GetMapping("/edit/{id}")
    public String editStaff(
            @PathVariable Long id, Model model) {

        model.addAttribute(
                "staff", staffService.getStaffById(id)
        );
        return "staff/form";
    }

    @PostMapping("/delete/{id}")
    public String deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return "redirect:/admin/staff";
    }

    @GetMapping("/search")
    public String searchStaff(
            @RequestParam String name, Model model) {

        model.addAttribute(
                "staffList", staffService.searchStaff(name)
        );
        return "staff/list";
    }
}