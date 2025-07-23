package com.bank.controller;

import com.bank.constant.SuccessMessages;
import com.bank.manager.WithdrawalManager;
import com.bank.representation.withdrawal.WithdrawalCreateRepresentation;
import com.bank.representation.withdrawal.WithdrawalRepresentation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WithdrawalController {

    @Autowired
    WithdrawalManager withdrawalManager;

    @GetMapping("/withdrawals")
    public ResponseEntity<List<WithdrawalRepresentation>> findAllWithdrawals() {
        return new ResponseEntity<>(withdrawalManager.listAllWithdrawals(), HttpStatus.OK);
    }

    @GetMapping("/withdrawals/{id}")
    ResponseEntity<WithdrawalRepresentation> findOneWithdrawal(@PathVariable Integer id){
        return new ResponseEntity<>(withdrawalManager.getWithdrawalById(id), HttpStatus.OK);
    }

    @GetMapping("withdrawals/account/{accountNumber}")
    ResponseEntity<List<WithdrawalRepresentation>> findWithdrawalsByAccount(@PathVariable Integer accountNumber){
        return new ResponseEntity<>(withdrawalManager.listWithdrawalsByAccountNumber(accountNumber), HttpStatus.OK);
    }

    @PostMapping(value = "/withdrawals", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createNewWithdrawal(@RequestBody @Valid WithdrawalCreateRepresentation withdrawalCreateRepresentation) {
        withdrawalManager.createWithdrawal(withdrawalCreateRepresentation);
        return new ResponseEntity<>(SuccessMessages.WITHDRAWAL_CREATED, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/withdrawals/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteWithdrawal(@PathVariable("id") @NotNull Integer transactionId){
        withdrawalManager.deleteWithdrawal(transactionId);
        return new ResponseEntity<>(SuccessMessages.WITHDRAWAL_DELETED, HttpStatus.NO_CONTENT);
    }
}
