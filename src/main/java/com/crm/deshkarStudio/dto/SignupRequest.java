package com.crm.deshkarStudio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(@NotBlank @Size(min=3, max=50) String username,
                            @NotBlank @Size(min=6, max=100) String password,
                            @Email @NotBlank String email,
                            @NotBlank @Size(min=7, max=15) String phone,
                            @NotBlank String role // "ADMIN"|"EMPLOYEE"|"USER"
) { }
