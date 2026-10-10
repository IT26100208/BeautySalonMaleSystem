package com.WD45.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.dto.UserFormDTO;
import com.WD45.BeautySalonMaleSystem.service.UserService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard")
public class AdminDashboardController {

    private final UserService userService;

    public AdminDashboardController(UserService userService) {
        this.userService = userService;
    }



    // ADMIN DASHBOARD


    @GetMapping
    public String dashboard(Model model) {

        model.addAttribute(
                "users",
                userService.getAllUsers()
        );

        return "admin/dashboard";
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