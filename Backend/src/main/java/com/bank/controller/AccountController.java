package com.bank.controller;

import com.bank.constant.SuccessMessages;
import com.bank.domain.Account;
import com.bank.domain.Card;
import com.bank.domain.Money;
import com.bank.manager.AccountManager;
import com.bank.mapper.AccountMapper;
import com.bank.repository.AccountRepository;
import com.bank.repository.CardRepository;
import com.bank.representation.account.AccountRepresentationRequest;
import com.bank.representation.account.AccountRepresentationResponse;
import com.bank.util.Currency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.max;

@RestController
public class AccountController {
    @Autowired
    public AccountRepository accountRepository;
    @Autowired
    public CardRepository cardRepository;
    @Autowired
    public AccountMapper accountMapper;
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
    ResponseEntity<?> updateAccount(@PathVariable("id") @NotNull Integer accountNumber,@RequestBody @Valid AccountRepresentationRequest accountRepresentation) {
        accountManager.updateAccount(accountNumber, accountRepresentation);
        return new ResponseEntity<>(SuccessMessages.ACCOUNT_UPDATED, HttpStatus.NO_CONTENT);
    }


    @DeleteMapping(value = "/accounts/delete/{id}")
    ResponseEntity<String> deleteAccount(@PathVariable("id") @NotNull Integer accountNumber){
        accountManager.deleteAccount(accountNumber);
        return new ResponseEntity<>(SuccessMessages.ACCOUNT_DELETED, HttpStatus.NO_CONTENT);
    }

    //TODO Move this business logic to Card Manager
    @PostMapping(value="/accounts/addCard/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> addCardinList(@PathVariable("id") Integer accountNumber,
                                    @RequestParam(required = true) String pin,
                                    @RequestParam(required = true) String expirationDate,
                                    @RequestParam(required = true,defaultValue = "true") Boolean activated){
        if(accountNumber ==null){return new ResponseEntity<>("AccountNumber is null", HttpStatus.BAD_REQUEST);}
        if(!accountRepository.existsById(accountNumber)){
            return new ResponseEntity<>("No Account with that id", HttpStatus.NOT_FOUND);
        }

        LocalDateTime dateTime;
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            dateTime = LocalDateTime.parse(expirationDate, formatter);
        } catch (DateTimeParseException b){
            return new ResponseEntity<>("Something went wrong with the date parsing.", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        List<Card> cardList = cardRepository.findAll();
        List<Long> longList = new ArrayList<>();
        for (Card c : cardList){
            longList.add(c.getCardId());
        }
        Account account = accountRepository.getReferenceById(accountNumber);
        try{
            account.addCard(max(longList)+1,pin,dateTime,activated);
            cardRepository.save(new Card(max(longList)+1,pin,dateTime,activated,account));
        } catch (BadRequestException b){
            return new ResponseEntity<>("Card not added", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Card added", HttpStatus.OK);
    }

    @DeleteMapping(value = "/accounts/delete/card/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteCardFromList(@PathVariable("id") Integer accountNumber,
                                         @RequestParam(required = true) Long cardID){
        if(accountNumber==null){return new ResponseEntity<>("AccountNumber is null", HttpStatus.BAD_REQUEST);}
        if(!accountRepository.existsById(accountNumber)){
            return new ResponseEntity<>("No Account with that id", HttpStatus.NOT_FOUND);
        }
        Account account = accountRepository.getReferenceById(accountNumber);
        try{
            account.deleteCard(cardID);
            cardRepository.deleteById(cardID);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("No Card with that id", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Card Deleted!", HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/accounts/{de_activate}/card/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> activateCardFromList(@PathVariable("id") Integer accountNumber,
                                           @PathVariable("de_activate") String de_activate,
                                           @RequestParam(required = true) Long cardID){
        if(accountNumber==null){return new ResponseEntity<>("AccountNumber is null", HttpStatus.BAD_REQUEST);}
        if(!accountRepository.existsById(accountNumber)){
            return new ResponseEntity<>("No Account with that id", HttpStatus.NOT_FOUND);
        }
        Account account = accountRepository.getReferenceById(accountNumber);
        Card card = cardRepository.getReferenceById(cardID);
        try{
            if(Objects.equals(de_activate, "activate")) {
                account.activateCard(cardID);
                card.setActivated(true);
            }
            if(Objects.equals(de_activate, "deactivate")) {
                account.blockCard(cardID);
                card.setActivated(false);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("No Card with that id or Card's state not correct", HttpStatus.NOT_FOUND);
        }

        cardRepository.save(card);
        return new ResponseEntity<>(String.format("Card is now %sd!", de_activate), HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/accounts/transfer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> transferFunds(@PathVariable("id") Integer from,
                                    @RequestParam(required = true) Integer to,
                                    @RequestParam(required = true) BigDecimal amount){
    if(!accountRepository.existsById(to)){
        return new ResponseEntity<>("No Destination Account with that id", HttpStatus.NOT_FOUND);
    }
    if(from==null){return new ResponseEntity<>("Origin Account number is null", HttpStatus.BAD_REQUEST);}
    if(!accountRepository.existsById(from)){
        return new ResponseEntity<>("No Origin Account with that id", HttpStatus.NOT_FOUND);
        }
    if(amount.compareTo(new BigDecimal(0))<=0){
        return new ResponseEntity<>("Amount is 0 or less", HttpStatus.BAD_REQUEST);
    }
    Account source = accountRepository.getReferenceById(from);
    Account destination = accountRepository.getReferenceById(to);
    try{
        source.transferFunds(destination,new Money(amount, Currency.EUR));
    } catch (BadRequestException b){
        return new ResponseEntity<>("Insufficient funds", HttpStatus.BAD_REQUEST);
    }
    accountRepository.save(source);
    accountRepository.save(destination);
    return new ResponseEntity<>("Transfer made succesfully", HttpStatus.NO_CONTENT);
    }
}
