package com.bank.representation;

import com.bank.domain.Account;
import com.bank.domain.Card;
import com.bank.repository.AccountRepository;
import jakarta.inject.Inject;
import org.mapstruct.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {Collectors.class})
public abstract class CardMapper {

    @Inject
    AccountRepository accountRepository;

    @Mapping(target = "date", ignore = true)
    @Mapping(target = "activated", source = "card.activated")
    @Mapping(target = "accountNumber", source = "card.account.accountNumber")
    public abstract CardRepresentation cardToRepresentation(Card card);

    public abstract List<CardRepresentation> toRepresentationList(List<Card> cardList);

    @Mapping(target = "activated", source = "activated")
    @Mapping(target = "cardId", source = "cardId")
    @Mapping(target = "expirationDate", ignore = true)
    @Mapping(target = "account", ignore = true)
    public abstract Card toModel(CardRepresentation cardRepresentation);

    @AfterMapping
    public void resolveCalendarToString(Card card, @MappingTarget CardRepresentation cardRepresentation) {
        LocalDateTime calendar = card.getExpirationDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        cardRepresentation.date = calendar.format(formatter);
    }

    @AfterMapping
    public void resolveStringToCalendar(CardRepresentation cardRepresentation, @MappingTarget Card card){
        String dateString = cardRepresentation.date;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);

        card.setExpirationDate(dateTime);
    }

    @AfterMapping
    public void resolveIntegerToAccount(CardRepresentation cardRepresentation, @MappingTarget Card card){
        Account account = null;
        if(cardRepresentation.accountNumber != null){
            account = accountRepository.getReferenceById(cardRepresentation.accountNumber);
        }
        card.setAccount(account);
    }
}
