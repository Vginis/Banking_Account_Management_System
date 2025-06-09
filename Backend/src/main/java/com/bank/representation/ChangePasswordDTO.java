package com.bank.representation;

import jakarta.validation.constraints.NotBlank;

public class ChangePasswordDTO {
    @NotBlank
    public String username;
    @NotBlank
    public String newPassword;
    @NotBlank
    public String oldPassword;
}
