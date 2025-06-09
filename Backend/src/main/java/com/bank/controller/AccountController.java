package com.bank.controller;

import com.bank.constant.SuccessMessages;
import com.bank.manager.AccountManager;
import com.bank.representation.account.AccountRepresentationRequest;
import com.bank.representation.account.AccountRepresentationResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {
    @Autowired
    public AccountManager accountManager;

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountRepresentationResponse>> findAllAccounts() {
        return new ResponseEntity<>(accountManager.listAllAccounts(),HttpStatus.OK);
    }

    @GetMapping("/accounts/{id}")
    ResponseEntity<AccountRepresentationResponse> findOneAccount(@PathVariable @NotNull Integer id){
        return new ResponseEntity<>(accountManager.findAccountById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/accounts/new", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createNewAccount(@Valid @RequestBody AccountRepresentationRequest accountRepresentation) {
        accountManager.createAccount(accountRepresentation);
        return new ResponseEntity<>(SuccessMessages.ACCOUNT_CREATION, HttpStatus.CREATED);
    }

    @PutMapping(value = "/accounts/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> updateAccount(@PathVariable("id") @NotNull Integer accountNumber,@RequestBody @Valid AccountRepresentationRequest accountRepresentation) {
        accountManager.updateAccount(accountNumber, accountRepresentation);
        return new ResponseEntity<>(SuccessMessages.ACCOUNT_UPDATED, HttpStatus.NO_CONTENT);
    }


    @DeleteMapping(value = "/accounts/delete/{id}")
    ResponseEntity<String> deleteAccount(@PathVariable("id") @NotNull Integer accountNumber){
        accountManager.deleteAccount(accountNumber);
        return new ResponseEntity<>(SuccessMessages.ACCOUNT_DELETED, HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/accounts/transfer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> transferFunds(@PathVariable("id") Integer from,
                                    @RequestParam(required = true) Integer to,
                                    @RequestParam(required = true) @Positive Long amount){
        accountManager.transferFunds(from, to, amount);
        return new ResponseEntity<>(SuccessMessages.TRANSFER_MADE_SUCCESSFULLY, HttpStatus.NO_CONTENT);
    }
}
