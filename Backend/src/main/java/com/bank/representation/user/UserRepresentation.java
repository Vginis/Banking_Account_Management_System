package com.bank.representation.user;

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
    public boolean isAdmin;

    //TODO Change List<Integer> accountList to List<AccountRepresentation>
}
