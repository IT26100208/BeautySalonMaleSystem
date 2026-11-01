
package com.WD45.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.dto.CustomerDTO;
import com.WD45.BeautySalonMaleSystem.dto.UserFormDTO;
import com.WD45.BeautySalonMaleSystem.entity.Customer;
import com.WD45.BeautySalonMaleSystem.repository.UserRepository;
import com.WD45.BeautySalonMaleSystem.service.LoyaltyService;
import com.WD45.BeautySalonMaleSystem.service.UserService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final LoyaltyService loyaltyService;

    public CustomerController(UserService userService, UserRepository userRepository, LoyaltyService loyaltyService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.loyaltyService = loyaltyService;
    }

    // ==========================================
    // CUSTOMER HOME
    // ==========================================

    @GetMapping("/customer")
    public String customerHome(Authentication authentication, Model model) {
        Customer customer = userRepository.findByEmail(authentication.getName())
                .filter(Customer.class::isInstance)
                .map(Customer.class::cast)
                .orElseThrow(() -> new IllegalStateException("Customer account was not found."));
        model.addAttribute("customer", customer);
        return "customer/home";
    }

    @GetMapping("/customer/loyalty")
    public String loyalty(Authentication authentication, Model model) {
        Customer customer = userRepository.findByEmail(authentication.getName())
                .filter(Customer.class::isInstance)
                .map(Customer.class::cast)
                .orElseThrow(() -> new IllegalStateException("Customer account was not found."));
        model.addAttribute("customer", customer);
        model.addAttribute("pointsBalance", loyaltyService.getPointsBalance(customer));
        model.addAttribute("loyaltyHistory", loyaltyService.getHistory(customer));
        return "customer/loyalty";
    }

    // ==========================================
    // VIEW CUSTOMER PROFILE
    // ==========================================

    @GetMapping("/customer/profile")
    public String viewProfile(Model model) {

        CustomerDTO customer =
                userService.getCurrentCustomerProfile();

        UserFormDTO user = new UserFormDTO();

        user.setId(customer.getId());
        user.setFirstName(customer.getFirstName());
        user.setLastName(customer.getLastName());
        user.setEmail(customer.getEmail());
        user.setPhoneNumber(customer.getPhoneNumber());
        user.setAddress(customer.getAddress());

        model.addAttribute("user", user);
        model.addAttribute(
                "loyaltyNumber",
                customer.getLoyaltyNumber()
        );

        return "customer/profile";
    }

    // ==========================================
    // UPDATE CUSTOMER PROFILE
    // ==========================================

    @PostMapping("/customer/profile")
    public String updateProfile(
            @Valid @ModelAttribute("user") UserFormDTO user,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            CustomerDTO customer =
                    userService.getCurrentCustomerProfile();

            model.addAttribute(
                    "loyaltyNumber",
                    customer.getLoyaltyNumber()
            );

            return "customer/profile";
        }

        try {

            userService.updateCurrentCustomerProfile(user);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Profile updated successfully!"
            );

            return "redirect:/customer/profile";

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());

            CustomerDTO customer =
                    userService.getCurrentCustomerProfile();

            model.addAttribute(
                    "loyaltyNumber",
                    customer.getLoyaltyNumber()
            );

            return "customer/profile";
        }
    }
}
