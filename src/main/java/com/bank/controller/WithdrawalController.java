package com.bank.controller;

import com.bank.domain.Account;
import com.bank.domain.Deposit;
import com.bank.domain.Withdrawal;
import com.bank.repository.AccountRepository;
import com.bank.repository.WithdrawalRepository;
import com.bank.representation.WithdrawalMapper;
import com.bank.representation.WithdrawalRepresentation;
import com.bank.util.Money;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.bank.util.Currency.EUR;
import static java.util.Collections.max;

@RestController
public class WithdrawalController {
    @Autowired
    public WithdrawalRepository withdrawalRepository;
    @Autowired
    public AccountRepository accountRepository;
    @Autowired
    public WithdrawalMapper withdrawalMapper;

    @GetMapping("/withdrawals")
    public ResponseEntity<?> findAllWithdrawals() {
        return new ResponseEntity<>(withdrawalMapper.toRepresentationList(withdrawalRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("/withdrawals/{id}")
    ResponseEntity<?> findOneWithdrawal(@PathVariable Integer id){
        if(id==null){
            return new ResponseEntity<>("id is null", HttpStatus.BAD_REQUEST);
        }
        if(!withdrawalRepository.existsById(id)){
            return new ResponseEntity<>("Withdrawal doesn't exist", HttpStatus.NOT_FOUND);
        }
        Withdrawal withdrawal = withdrawalRepository.getReferenceById(id);
        return new ResponseEntity<>(withdrawalMapper.toRepresentation(withdrawal), HttpStatus.OK);
    }

    @PostMapping(value = "/withdrawals/new", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createNewWithdrawal(@RequestBody WithdrawalRepresentation withdrawalRepresentation) {
        if(withdrawalRepository.existsById(withdrawalRepresentation.transactionId)){
            return new ResponseEntity<>("There is another withdrawal with that id", HttpStatus.BAD_REQUEST);
        }
        if (withdrawalRepresentation.amount.compareTo(new BigDecimal(0))<=0|| withdrawalRepresentation.amount.compareTo(new BigDecimal(1000))>=0){
            return new ResponseEntity<>("Invalid Amount to withdrawal", HttpStatus.BAD_REQUEST);
        }
        try {
            Withdrawal withdrawal = withdrawalMapper.toModel(withdrawalRepresentation);
            withdrawalRepository.save(withdrawal);
            return new ResponseEntity<>("Withdrawal Created!", HttpStatus.CREATED);
        } catch (Exception p) {
            return new ResponseEntity<>("Something went wrong.Possible format error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/withdrawals/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateWithdrawal(@PathVariable("id") Integer transactionId,@RequestBody WithdrawalRepresentation withdrawalRepresentation) throws ParseException {
        if(transactionId ==null || withdrawalRepresentation==null){return new ResponseEntity<>("Deposit or id is null", HttpStatus.BAD_REQUEST);}
        if(!withdrawalRepository.existsById(transactionId)){
            return new ResponseEntity<>("No Withdrawal with that id", HttpStatus.NOT_FOUND);
        }
        if (withdrawalRepresentation.amount.compareTo(new BigDecimal(0))<=0|| withdrawalRepresentation.amount.compareTo(new BigDecimal(1000))>=0){
            return new ResponseEntity<>("Invalid Amount to withdrawal", HttpStatus.BAD_REQUEST);
        }
        Withdrawal withdrawal1 = withdrawalRepository.getReferenceById(transactionId);
        Withdrawal withdrawal = withdrawalMapper.toModel(withdrawalRepresentation);

        withdrawal1.setDate(withdrawal.getDate());
        withdrawal1.setAccount(withdrawal.getAccount());
        withdrawal1.setAmount(withdrawal.getAmount());
        withdrawal1.setTransactionId(withdrawal.getTransactionId());

        withdrawalRepository.save(withdrawal1);

        return new ResponseEntity<>("Withdrawal Updated!", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/withdrawals/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteWithdrawal(@PathVariable("id") Integer transactionId){
        if(transactionId==null){return new ResponseEntity<>("transactionId is null", HttpStatus.BAD_REQUEST);}
        if(!withdrawalRepository.existsById(transactionId)){
            return new ResponseEntity<>("No Withdrawal with that id", HttpStatus.NOT_FOUND);
        }
        withdrawalRepository.deleteById(transactionId);
        return new ResponseEntity<>("Withdrawal Deleted!", HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/withdrawals/make/{account}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> makeWithdrawal(@PathVariable("account") Integer account,
                                  @RequestParam("amount") BigDecimal amount){
        if(account==null){return new ResponseEntity<>("account is null", HttpStatus.BAD_REQUEST);}
        if(!accountRepository.existsById(account)){
            return new ResponseEntity<>("No Account with that id", HttpStatus.NOT_FOUND);
        }
        if (amount.compareTo(new BigDecimal(0))<=0|| amount.compareTo(new BigDecimal(1000))>=0){
            return new ResponseEntity<>("Invalid Amount to withdrawal", HttpStatus.BAD_REQUEST);
        }
        Account account1 = accountRepository.getReferenceById(account);
        List<Integer> intList = new ArrayList<>();
        for (Withdrawal d: withdrawalRepository.findAll()){
            intList.add(d.getTransactionId());
        }
        Withdrawal withdrawal = new Withdrawal( max(intList)+1, LocalDateTime.now(),new Money(amount,EUR),account1);

        try{
            withdrawal.makeWithdrawal(withdrawal.getAmount().getAmount(), account1);
        } catch (BadRequestException e){
            return new ResponseEntity<>("Insufficient Funds", HttpStatus.BAD_REQUEST);
        }

        accountRepository.save(account1);
        return new ResponseEntity<>("Withdrawal Made!", HttpStatus.NO_CONTENT);
    }
}
