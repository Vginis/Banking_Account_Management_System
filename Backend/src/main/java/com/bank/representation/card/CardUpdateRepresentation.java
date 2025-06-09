package com.bank.representation.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CardUpdateRepresentation {
    @NotBlank
    @Pattern(regexp = "\\d{4}", message = "PIN must be exactly 4 digits")
    public String pin;
    @NotBlank
    public String expirationDate;
    @NotNull
    public boolean activated;
}
