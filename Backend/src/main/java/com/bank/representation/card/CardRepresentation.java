package com.bank.representation.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
public class CardRepresentation {
    @NotNull
    public Long cardId;
    public String pin;
    @NotBlank
    public String expirationDate;
    @NotNull
    public boolean activated;
    @NotNull
    public Integer accountNumber;
}
