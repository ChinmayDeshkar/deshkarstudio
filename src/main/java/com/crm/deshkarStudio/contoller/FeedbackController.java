package com.crm.deshkarStudio.contoller;

import com.crm.deshkarStudio.model.Feedback;
import com.crm.deshkarStudio.services.FeedbackService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/feedback")
@AllArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("/check/phone-number/{phoneNumber}")
    public Boolean phoneNumberExists(@PathVariable Long phoneNumber){
        log.debug("Checking if number exists, " + phoneNumber);
        return feedbackService.phoneNumberExists(phoneNumber);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFeedback(@RequestBody Feedback feedback){
        if(feedbackService.phoneNumberExists(feedback.getPhoneNumber())){
            throw new RuntimeException("Phone Number already present");
        }
        try{
            log.debug("Adding feedback for phone Number: " + feedback.getPhoneNumber());
            feedbackService.addFeedback(feedback);

            return ResponseEntity.ok(Map.of("message", "Feedback added successfully!!"));
        }catch (Exception e){
            throw new RuntimeException("Error while adding feedback, " + e);
        }
    }
}
