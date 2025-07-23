package com.bank.controller;

import com.bank.constant.SuccessMessages;
import com.bank.manager.DepositManager;
import com.bank.representation.deposit.DepositCreateRepresentation;
import com.bank.representation.deposit.DepositRepresentation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


@RestController
public class DepositController {
    @Autowired
    DepositManager depositManager;

    @GetMapping("/deposits")
    public ResponseEntity<List<DepositRepresentation>> findAllDeposits() {
        return new ResponseEntity<>(depositManager.findAllDeposits(), HttpStatus.OK);
    }

    @GetMapping("/deposits/{id}")
    ResponseEntity<DepositRepresentation> findOneDeposit(@PathVariable @NotNull Integer id){
        return new ResponseEntity<>(depositManager.findDepositById(id), HttpStatus.OK);
    }

    @GetMapping("deposits/account/{accountNumber}")
    ResponseEntity<List<DepositRepresentation>> findDepositsByAccount(@PathVariable @NotNull Integer accountNumber){
        return new ResponseEntity<>(depositManager.findDepositsByAccount(accountNumber), HttpStatus.OK);
    }

    @PostMapping(value = "/deposits", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createNewDeposit(@RequestBody @Valid DepositCreateRepresentation depositCreateRepresentation) {
        depositManager.createDeposit(depositCreateRepresentation);
        return new ResponseEntity<>(SuccessMessages.DEPOSIT_CREATED, HttpStatus.CREATED);
    }

    @PutMapping(value = "/deposits/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> updateDeposit(@PathVariable("id") Integer transactionId,@RequestBody @Valid DepositCreateRepresentation depositRepresentation) throws ParseException {
        depositManager.updateDeposit(transactionId, depositRepresentation);
        return new ResponseEntity<>(SuccessMessages.DEPOSIT_UPDATED, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/deposits/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteDeposit(@PathVariable("id") Integer transactionId){
        depositManager.deleteDeposit(transactionId);
        return new ResponseEntity<>(SuccessMessages.DEPOSIT_DELETED, HttpStatus.NO_CONTENT);
    }
}
