package com.WD45.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.dto.PasswordChangeDTO;
import com.WD45.BeautySalonMaleSystem.dto.ProfileUpdateDTO;
import com.WD45.BeautySalonMaleSystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    private final UserService userService;

    public SettingsController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String showSettingsPage(
            Model model,
            Principal principal) {

        var customer =
                userService.getCurrentCustomerProfile();

        ProfileUpdateDTO profile = new ProfileUpdateDTO();

        profile.setFullName(
                customer.getFirstName() + " " +
                        customer.getLastName()
        );

        profile.setEmail(customer.getEmail());
        profile.setPhone(customer.getPhoneNumber());

        model.addAttribute("profile", profile);
        model.addAttribute(
                "passwordDTO",
                new PasswordChangeDTO()
        );

        return "settings";
    }


    @PostMapping("/profile")
    public String updateProfile(
            @ModelAttribute("profile")
            ProfileUpdateDTO profileDTO,
            Principal principal) {

        try {
            userService.updateProfile(
                    principal.getName(),
                    profileDTO
            );

            return "redirect:/settings?profileSuccess=true";

        } catch (IllegalArgumentException ex) {

            return "redirect:/settings?profileError=true";
        }
    }


    @PostMapping("/password")
    public String updatePassword(
            @ModelAttribute("passwordDTO")
            PasswordChangeDTO passwordDTO,
            Principal principal) {

        boolean updated = userService.updatePassword(
                principal.getName(),
                passwordDTO
        );

        if (!updated) {
            return "redirect:/settings?passwordError=true";
        }

        return "redirect:/settings?passwordSuccess=true";
    }

}
