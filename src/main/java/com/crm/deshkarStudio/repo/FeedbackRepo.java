package com.crm.deshkarStudio.repo;

import com.crm.deshkarStudio.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepo extends JpaRepository<Feedback, Long> {

    boolean existsByPhoneNumber(Long phoneNumber);
}
