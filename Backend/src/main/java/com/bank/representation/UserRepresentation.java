package com.bank.representation;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import java.util.List;

@RegisterReflectionForBinding
public class UserRepresentation {
    public Integer userId;
    public String firstName;
    public String lastName;
    public String username;
    public String email;
    public String address;
    public List<Integer> accountList;
}
