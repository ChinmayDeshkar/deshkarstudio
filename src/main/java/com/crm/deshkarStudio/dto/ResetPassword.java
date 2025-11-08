package com.crm.deshkarStudio.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResetPassword {
    private String username;
    private String oldPassword;
    private String newPassword;
}
