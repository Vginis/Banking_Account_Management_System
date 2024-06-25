package com.bank.representation;

import com.bank.domain.Account;
import com.bank.domain.Address;
import com.bank.domain.User;
import com.bank.repository.AccountRepository;
import jakarta.inject.Inject;
import org.apache.coyote.BadRequestException;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {Collectors.class},
        uses = {AccountMapper.class})
public abstract class UserMapper {
    @Inject
    AccountRepository accountRepository;

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "username", target="username")
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "accountList", expression = "java(user.getAccountList().stream().map(com.bank.domain.Account::getAccountNumber).collect(Collectors.toList()))")
    public abstract UserRepresentation userToRepresentation(User user);

    public abstract List<UserRepresentation> toRepresentationList(List<User> userList);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target="username")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "accountList",ignore = true)
    public abstract User userRepresentationToModel(UserRepresentation userRepresentation) throws BadRequestException;

    @AfterMapping
    public void resolveAddressToString(User user, @MappingTarget UserRepresentation userRepresentation) {
        String address = "";
        if (user.getAddress() != null){
            address += user.getAddress().getStreet() + " " + user.getAddress().getNumber() + " " + user.getAddress().getZipCode();}
        userRepresentation.address = address;
    }

    @AfterMapping
    public void resolveStringToAddress(UserRepresentation userRepresentation,@MappingTarget User user){
        String add = userRepresentation.address;
        Address address = new Address();
        String[] wordList = new String[3];
        wordList = add.split(" ");
        address.setStreet(wordList[0]);
        address.setNumber(wordList[1]);
        address.setZipCode(wordList[2]);
        user.setAddress(address);
    }

    @AfterMapping
    public void resolveAccountList(UserRepresentation userRepresentation, @MappingTarget User user){
        List<Account> accountList = new ArrayList<>(userRepresentation.accountList.size());
        Account account =null;
        for (Integer i : userRepresentation.accountList){
            if(i!=null){
                account = accountRepository.getReferenceById(i);
            }
            accountList.add(account);
        }
        user.setAccountList(accountList);
    }

    @AfterMapping
    public void resolvePassword(@MappingTarget User user){
        user.setPassword("temporary");
    }
}
