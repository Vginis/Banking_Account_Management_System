package com.bank.representation.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public class UpdateUserRepresentation {
    @NotBlank
    public String firstName;
    @NotBlank
    public String lastName;
    @Email
    public String email;
    @NotBlank
    public String address;
}
