package com.crm.deshkarStudio.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Long purchaseId;
    private String updatedBy;
    private String note;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dte_updated = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
}
