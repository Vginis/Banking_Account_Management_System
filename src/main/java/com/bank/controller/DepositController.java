package com.bank.controller;

import com.bank.domain.Account;
import com.bank.domain.Deposit;
import com.bank.repository.AccountRepository;
import com.bank.repository.DepositRepository;
import com.bank.representation.DepositMapper;
import com.bank.representation.DepositRepresentation;
import com.bank.util.Currency;
import com.bank.util.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.bank.util.Currency.EUR;
import static java.util.Collections.max;


@RestController
public class DepositController {
    @Autowired
    public DepositRepository depositRepository;
    @Autowired
    public AccountRepository accountRepository;
    @Autowired
    public DepositMapper depositMapper;
    @GetMapping("/deposits")
    public ResponseEntity<?> findAllDeposits() {
        return new ResponseEntity<>(depositMapper.toRepresentationList(depositRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("/deposits/{id}")
    ResponseEntity<?> findOneDeposit(@PathVariable Integer id){
        if(id==null){
            return new ResponseEntity<>("id is null", HttpStatus.BAD_REQUEST);
        }
        if(!depositRepository.existsById(id)){
            return new ResponseEntity<>("Deposit doesn't exist", HttpStatus.NOT_FOUND);
        }
        Deposit deposit = depositRepository.getReferenceById(id);
        return new ResponseEntity<>(depositMapper.toRepresentation(deposit), HttpStatus.OK);
    }

    @PostMapping(value = "/deposits/new", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createNewDeposit(@RequestBody DepositRepresentation depositRepresentation) {
        if(depositRepository.existsById(depositRepresentation.transactionId)){
            return new ResponseEntity<>("There is another deposit with that id", HttpStatus.BAD_REQUEST);
        }
        try {
            Deposit deposit = depositMapper.toModel(depositRepresentation);
            depositRepository.save(deposit);
            return new ResponseEntity<>("Deposit Created!", HttpStatus.CREATED);
        } catch (Exception p) {
            return new ResponseEntity<>("Something went wrong.Possible format error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/deposits/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateDeposit(@PathVariable("id") Integer transactionId,@RequestBody DepositRepresentation depositRepresentation) throws ParseException {
        if(transactionId ==null || depositRepresentation==null){return new ResponseEntity<>("Deposit or id is null", HttpStatus.BAD_REQUEST);}
        if(!depositRepository.existsById(transactionId)){
            return new ResponseEntity<>("No Deposit with that id", HttpStatus.NOT_FOUND);
        }
        Deposit deposit1 = depositRepository.getReferenceById(transactionId);
        Deposit deposit = depositMapper.toModel(depositRepresentation);

        deposit1.setDate(deposit.getDate());
        deposit1.setAccount(deposit.getAccount());
        deposit1.setAmount(deposit.getAmount());
        deposit1.setTransactionId(deposit.getTransactionId());

        depositRepository.save(deposit1);

        return new ResponseEntity<>("Deposit Updated!", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/deposits/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteDeposit(@PathVariable("id") Integer transactionId){
        if(transactionId==null){return new ResponseEntity<>("transactionId is null", HttpStatus.BAD_REQUEST);}
        if(!depositRepository.existsById(transactionId)){
            return new ResponseEntity<>("No Deposit with that id", HttpStatus.NOT_FOUND);
        }
        depositRepository.deleteById(transactionId);
        return new ResponseEntity<>("Deposit Deleted!", HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/deposits/make/{account}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> makeDeposit(@PathVariable("account") Integer account,
                                  @RequestParam("amount") BigDecimal amount){
        if(account==null){return new ResponseEntity<>("account is null", HttpStatus.BAD_REQUEST);}
        if(!accountRepository.existsById(account)){
            return new ResponseEntity<>("No Account with that id", HttpStatus.NOT_FOUND);
        }

        Account account1 = accountRepository.getReferenceById(account);
        List<Integer> intList = new ArrayList<>();
        for (Deposit d: depositRepository.findAll()){
            intList.add(d.getTransactionId());
        }
        Deposit deposit = new Deposit( max(intList)+1,LocalDateTime.now(),new Money(amount,EUR),account1);

        deposit.makeDeposit(deposit.getAmount().getAmount(), account1);
        accountRepository.save(account1);
        return new ResponseEntity<>("Deposit Made!", HttpStatus.NO_CONTENT);
    }
}
