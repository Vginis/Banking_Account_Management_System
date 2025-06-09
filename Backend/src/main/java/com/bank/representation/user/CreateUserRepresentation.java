package com.bank.representation.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateUserRepresentation {
    @NotBlank
    public String firstName;
    @NotBlank
    public String lastName;
    @NotBlank
    public String username;
    @NotBlank
    public String password;
    @Email
    public String email;
    @NotBlank
    public String address;
    @NotNull
    public boolean isAdmin;
}
