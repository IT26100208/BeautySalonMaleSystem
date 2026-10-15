package com.WD45.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.BeautySalonMaleSystem.dto.PasswordChangeDTO;
import com.WD45.BeautySalonMaleSystem.BeautySalonMaleSystem.dto.ProfileUpdateDTO;
import com.WD45.BeautySalonMaleSystem.service.UserService;
import org.springframework.ui.Model;

import java.security.Principal;

public class SettingsController {
    private final UserService userService;

    public SettingsController(UserService userService) {
        this.userService = userService;
    }

    public String showSettingsPage(Model model, Principal principal) {
        model.addAttribute("profile", new ProfileUpdateDTO());
        model.addAttribute("passwordDTO", new PasswordChangeDTO());
        return "settings";
    }

    public String updateProfile(ProfileUpdateDTO profileDTO, Principal principal) {
        userService.updateProfile(principal.getName(), profileDTO);
        return "redirect:/settings?profileSuccess=true";
    }

    public String updatePassword(PasswordChangeDTO passwordDTO, Principal principal) {
        boolean update = userService.updatePassword(principal.getName(), passwordDTO);
        if (!update) {
            return "redirect:/settings?passwordError=true";
        }
        return "redirect:/setting?passwordSuccess=true";
    }
}
