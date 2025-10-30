package com.crm.deshkarStudio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String phoneNumber;
    private String otp;
    private LocalDateTime expiryTime;
}
