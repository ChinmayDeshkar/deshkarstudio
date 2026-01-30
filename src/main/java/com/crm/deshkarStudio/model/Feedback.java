package com.crm.deshkarStudio.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "tb_feedback")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long phoneNumber;
    int overallRating;
    int serviceQuality;
    String serviceType;
    String comment;
    boolean wouldRecommend;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime submittedAt = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

}
