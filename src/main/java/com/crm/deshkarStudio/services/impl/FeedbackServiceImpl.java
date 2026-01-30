package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.model.Feedback;
import com.crm.deshkarStudio.repo.FeedbackRepo;
import com.crm.deshkarStudio.services.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepo feedbackRepo;

    @Override
    public Feedback addFeedback(Feedback feedback) {
        return feedbackRepo.save(feedback);
    }

    @Override
    public Boolean phoneNumberExists(Long phoneNumber) {
        return feedbackRepo.existsByPhoneNumber(phoneNumber);
    }
}
