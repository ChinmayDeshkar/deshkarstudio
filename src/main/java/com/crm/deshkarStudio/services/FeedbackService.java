package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.model.Feedback;

public interface FeedbackService {

    Feedback addFeedback(Feedback feedback);

    Boolean phoneNumberExists(Long phoneNumber);

}
