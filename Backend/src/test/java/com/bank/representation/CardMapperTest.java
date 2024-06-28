package com.bank.representation;

import com.bank.Initialization;
import com.bank.domain.Card;
import com.bank.Fixture;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CardMapperTest extends Initialization {
    @Inject
    CardMapper cardMapper;

    @Transactional
    @Test
    public void testToRepresentation(){
        CardRepresentation cardRepresentation = cardMapper.cardToRepresentation(card);
        assertEquals(cardRepresentation.accountNumber,card.getAccount().getAccountNumber());
        assertEquals(cardRepresentation.cardId,card.getCardId());
        assertEquals(cardRepresentation.date,"2030-11-26 00:00:00");
        assertEquals(cardRepresentation.pin,card.getPin());
        assertEquals(cardRepresentation.activated,card.getActivated().toString());
    }

    @Transactional
    @Test
    public void testToModel() throws ParseException {
        fixture = new Fixture();
        CardRepresentation cardRepresentation = fixture.createCardRepresentation();

        Card card2 = cardMapper.toModel(cardRepresentation);
        assertEquals(card2.getActivated().toString(),cardRepresentation.activated);
        assertEquals(card2.getCardId(), cardRepresentation.cardId);
        assertEquals(card2.getExpirationDate().plusYears(3),localDateTime);
        assertEquals(card2.getPin(), cardRepresentation.pin);
        assertEquals(card2.getAccount().getAccountNumber(),cardRepresentation.accountNumber);
    }

}
