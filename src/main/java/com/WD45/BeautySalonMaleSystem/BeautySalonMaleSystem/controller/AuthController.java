package com.WD45.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.dto.UserFormDTO;
import com.WD45.BeautySalonMaleSystem.service.UserService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }



    // LOGIN PAGE


    @GetMapping("/login")
    public String loginPage() {

        return "login";
    }



    // HOME --- > LOGIN


    @GetMapping("/")
    public String home() {

        return "redirect:/login";
    }


    // REGISTER PAGE

    @GetMapping("/register")
    public String registerPage(Model model) {

        UserFormDTO user = new UserFormDTO();

        model.addAttribute(
                "user",
                user
        );

        return "register";
    }


    // REGISTER CUSTOMER

    @PostMapping("/register")
    public String registerUser(

            @Valid
            @ModelAttribute("user")
            UserFormDTO user,

            BindingResult result,

            Model model) {

        // Validation errors

        if (result.hasErrors()) {

            return "register";
        }


        try {

            // All registrations are customers

            user.setUserType("CUSTOMER");

            userService.registerCustomer(user);

            return "redirect:/login?registered";


        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "error",
                    e.getMessage()
            );

            return "register";
        }
    }
}