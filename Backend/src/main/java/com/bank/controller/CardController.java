package com.bank.controller;

import com.bank.constant.SuccessMessages;
import com.bank.manager.CardManager;
import com.bank.representation.card.CardRepresentation;
import com.bank.representation.card.CardUpdateRepresentation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardController {
    @Autowired
    CardManager cardManager;

    @GetMapping("/cards")
    List<CardRepresentation> findAllCard(){
        return cardManager.findAllCards();
    }

    @GetMapping("/cards/{id}")
    CardRepresentation findOneCard(@PathVariable Long id){
       return cardManager.findCardById(id);
    }

    @GetMapping("/cards/account/{accountNumber}")
    List<CardRepresentation> findCardByAccount(@PathVariable Integer accountNumber){
        return cardManager.findCardByAccount(accountNumber);
    }

    @PostMapping(value = "/cards/new", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createNewCard(@RequestBody @Valid CardRepresentation cardRepresentation) {
        cardManager.createNewCard(cardRepresentation);
        return new ResponseEntity<>(SuccessMessages.CARD_CREATION,HttpStatus.CREATED);
    }

    @PutMapping(value = "/cards/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> updateCard(@PathVariable("id") @NotNull Long cardId, @RequestBody @Valid CardUpdateRepresentation cardRepresentation) {
        cardManager.updateCard(cardId, cardRepresentation);
        return new ResponseEntity<>(SuccessMessages.CARD_UPDATED, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/cards/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteCard(@PathVariable("id") @NotNull Long cardId){
        cardManager.deleteCard(cardId);
        return new ResponseEntity<>(SuccessMessages.CARD_DELETED, HttpStatus.NO_CONTENT);
    }
}
