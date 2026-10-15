package com.WD45.BeautySalonMaleSystem.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.BeautySalonMaleSystem.dto.FeedbackDTO;
import com.WD45.BeautySalonMaleSystem.BeautySalonMaleSystem.entity.Feedback;
import com.WD45.BeautySalonMaleSystem.BeautySalonMaleSystem.repository.FeedbackRepository;

import java.util.List;

public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public void submitFeedback(FeedbackDTO feedbackDTO, String userEmail) {
        Feedback feedback = new Feedback();
        feedback.setCustomerEmail(userEmail);
        feedback.setCustomerName(userEmail != null ? userEmail.split("@")[0] : "Anonymous");
        feedback.setComment(feedbackDTO.getComment());
        feedback.setRating(feedbackDTO.getRating());
        feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc();
    }
}
