package com.WD45.BeautySalonMaleSystem.BeautySalonMaleSystem.controller;

import com.WD45.BeautySalonMaleSystem.BeautySalonMaleSystem.dto.FeedbackDTO;
import com.WD45.BeautySalonMaleSystem.BeautySalonMaleSystem.service.FeedbackService;
import org.springframework.ui.Model;

import java.security.Principal;

public class CustomerFeedbackController {
    private final FeedbackService feedbackService;

    public CustomerFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    public String showFeedbackController(Model model) {
        model.addAttribute("feedback", new FeedbackDTO());
        return "customer/feedback";
    }

    public String submitFeedback(FeedbackDTO feedbackDTO, Principal principal) {
        String email = principal != null ? principal.getName() : "anonymous@salon.com";
        feedbackService.submitFeedback(feedbackDTO, email);
        return "redirect:/customer/feedback?submitted=true";
    }
}
