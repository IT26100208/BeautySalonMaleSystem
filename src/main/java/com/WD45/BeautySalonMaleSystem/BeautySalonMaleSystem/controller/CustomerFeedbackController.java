package com.WD45.BeautySalonMaleSystem.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.BeautySalonMaleSystem.dto.FeedbackDTO;
import com.WD45.BeautySalonMaleSystem.BeautySalonMaleSystem.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/customer/feedback")
public class CustomerFeedbackController {
    private final FeedbackService feedbackService;

    public CustomerFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public String showFeedbackController(Model model) {
        model.addAttribute("feedback", new FeedbackDTO());
        return "customer/feedback";
    }

    @PostMapping
    public String submitFeedback(@ModelAttribute("feedback") FeedbackDTO feedbackDTO, Principal principal) {
        String email = principal != null ? principal.getName() : "anonymous@salon.com";
        feedbackService.submitFeedback(feedbackDTO, email);
        return "redirect:/customer/feedback?submitted=true";
    }
}
