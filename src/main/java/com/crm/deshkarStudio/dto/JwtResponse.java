package com.crm.deshkarStudio.dto;

public record JwtResponse(
        String accessToken,
        String tokenType,
        String username,
        String role
) {}