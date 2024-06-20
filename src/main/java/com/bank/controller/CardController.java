package com.bank.controller;

import com.bank.domain.Card;
import com.bank.repository.AccountRepository;
import com.bank.repository.CardRepository;
import com.bank.representation.CardMapper;
import com.bank.representation.CardRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class CardController {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final AccountRepository accountRepository;
    CardController(CardRepository cardRepository, CardMapper cardMapper, AccountRepository accountRepository){
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.accountRepository = accountRepository;
    }
    //todo fix the responses here
    @GetMapping("/cards")
    List<CardRepresentation> findAllCard(){
        return cardMapper.toRepresentationList(cardRepository.findAll());
    }
    @GetMapping("/cards/{id}")
    CardRepresentation findOneCard(@PathVariable Long id){
       Card card = cardRepository.findCardById(id);
       if(card == null){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Card not found");
       }
       return cardMapper.cardToRepresentation(card);
    }

    @GetMapping("/cards/account/{accountNumber}")
    List<CardRepresentation> findCardByAccount(@PathVariable Integer accountNumber){
        return cardMapper.toRepresentationList(cardRepository.findCardByAccount(accountRepository.getReferenceById(accountNumber)));
    }

    @PostMapping(value = "/cards/new", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createNewCard(@RequestBody CardRepresentation cardRepresentation) {
        if(cardRepresentation==null){
            return new ResponseEntity<>("Card is null", HttpStatus.BAD_REQUEST);
        }
        if(cardRepository.existsById(cardRepresentation.cardId)){
            return new ResponseEntity<>("There is another card with that id", HttpStatus.BAD_REQUEST);
        }
        try {
            Card card = cardMapper.toModel(cardRepresentation);
            cardRepository.save(card);
            return new ResponseEntity<>("Card Created!", HttpStatus.CREATED);
        } catch (Exception p) {
            return new ResponseEntity<>("Something went wrong.Possible format error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/cards/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateCard(@PathVariable("id") Long cardId,@RequestBody CardRepresentation cardRepresentation) {
        if(cardId ==null || cardRepresentation==null){return new ResponseEntity<>("Card or id is null", HttpStatus.BAD_REQUEST);}
        if(!cardRepository.existsById(cardId)){
            return new ResponseEntity<>("No card with that id", HttpStatus.NOT_FOUND);
        }
        Card card1 = cardRepository.getReferenceById(cardId);
        Card card = cardMapper.toModel(cardRepresentation);

        card1.setCardId(card.getCardId());
        card1.setAccount(card.getAccount());
        card1.setActivated(card.getActivated());
        card1.setPin(card.getPin());
        card1.setExpirationDate(card.getExpirationDate());

        cardRepository.save(card1);

        return new ResponseEntity<>("Card Updated!", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/cards/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteCard(@PathVariable("id") Long cardId){
        if(cardId==null){return new ResponseEntity<>("Id is null", HttpStatus.BAD_REQUEST);}
        if(!cardRepository.existsById(cardId)){
            return new ResponseEntity<>("No card with that id", HttpStatus.NOT_FOUND);
        }
        cardRepository.deleteById(cardId);
        return new ResponseEntity<>("Card Deleted!", HttpStatus.NO_CONTENT);
    }
}
