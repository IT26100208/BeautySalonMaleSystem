package com.WD45.BeautySalonMaleSystem.service;

import com.WD45.BeautySalonMaleSystem.dto.FeedbackDTO;
import com.WD45.BeautySalonMaleSystem.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    void submitFeedback(FeedbackDTO feedbackDTO, String userEmail);
    List<Feedback> getAllFeedback();
}
