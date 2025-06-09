package com.bank.manager;

import com.bank.constant.ErrorMessages;
import com.bank.domain.Card;
import com.bank.exception.EntityNotFoundException;
import com.bank.exception.IllegalArgumentException;
import com.bank.mapper.CardMapper;
import com.bank.repository.AccountRepository;
import com.bank.repository.CardRepository;
import com.bank.representation.card.CardRepresentation;
import com.bank.representation.card.CardUpdateRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CardManager {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    CardMapper cardMapper;
    @Autowired
    AccountRepository accountRepository;

    public List<CardRepresentation> findAllCards(){
        return cardMapper.toRepresentationList(cardRepository.findAll());
    }

    public CardRepresentation findCardById(Long id){
        Optional<Card> cardOptional = cardRepository.findById(id);
        if(cardOptional.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }
        return cardMapper.cardToRepresentation(cardOptional.get());
    }

    public List<CardRepresentation> findCardByAccount(Integer accountNumber){
        return cardMapper.toRepresentationList(cardRepository.findCardByAccount(accountRepository.getReferenceById(accountNumber)));
    }

    public void createNewCard(CardRepresentation cardRepresentation){
        if(cardRepository.existsById(cardRepresentation.cardId)){
            throw new IllegalArgumentException("card_already_exists");
        }

        if(!accountRepository.existsById(cardRepresentation.accountNumber)){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Card card = cardMapper.toModel(cardRepresentation);
        cardRepository.save(card);
    }

    public void updateCard(Long cardId, CardUpdateRepresentation cardRepresentation){
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if(cardOptional.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Card card = cardOptional.get();
        card.setActivated(cardRepresentation.activated);
        card.setPin(cardRepresentation.pin);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        card.setExpirationDate(LocalDateTime.parse(cardRepresentation.expirationDate,formatter));

        cardRepository.save(card);
    }

    public void deleteCard(Long cardId){
        if(!cardRepository.existsById(cardId)){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }
        cardRepository.deleteById(cardId);
    }
}
