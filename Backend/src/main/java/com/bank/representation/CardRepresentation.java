package com.bank.representation;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
public class CardRepresentation {
    public Long cardId;
    public String pin;
    public String date;
    public String activated;
    public Integer accountNumber;
}
