package com.bank.domain;


import com.bank.Initialization;
import com.bank.util.Money;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.bank.util.Currency.EUR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AccountTest extends Initialization {

    /*Getter-Setter tests*/
    @Test
    public void getAccountNumberTest(){assertEquals(2,account.getAccountNumber());}

    @Test
    public void getUserTest(){assertEquals(1000,account.getUser().getUserId());}

    @Test
    public void getBalanceTest(){assertEquals(new BigDecimal(45),account.getBalance().getAmount());}

    @Test
    public void getCardListTest(){assertEquals(1,account.getCardList().size());}

    @Test
    public void setUserTest(){
        User user2 = new User(1001,"Kostas","Mitoglou","kmitoglou@gmail.com", new Address(), accountList);
        account.setUser(user2);
        assertEquals(1001,account.getUser().getUserId());
    }

    @Test
    public void setBalanceTest(){
        Integer newAmount = 56;
        account.setBalance(new Money(new BigDecimal(56),EUR));
        assertEquals(new BigDecimal(56),account.getBalance().getAmount());
    }

    @Test
    public void setListCardTest(){
        List<Card> newList = new ArrayList<>();
        account.setCardList(newList);
        assertEquals(0,newList.size());
    }

    /*domain logic tests*/
    @Test
    public void addCard() throws ParseException, BadRequestException {
        account.addCard(444444444L, "1234",localDateTime, true);
        assertEquals(2,listCard.size());
    }

    @Test
    public void addCardNullValue() throws ParseException, BadRequestException {
        assertThrows(BadRequestException.class, () -> {
            account.addCard(null, "1234",localDateTime, true);
        });
    }

    @Test
    public void addCardWithSameId() throws ParseException, BadRequestException {
        assertThrows(BadRequestException.class, () -> {
            account.addCard(111111111L, "1234",localDateTime, true);
        });
    }

    @Test
    public void addCardWithInvalidDate() throws ParseException, BadRequestException {
        assertThrows(BadRequestException.class, () -> {
            account.addCard(1234567L, "1234",localDateTime.minusYears(10), true);
        });
    }

    @Test
    public void addCardWithInvalidPin() throws ParseException, BadRequestException {
        assertThrows(BadRequestException.class, () -> {
            account.addCard(1234567L, "pin",localDateTime, true);
        });
    }

    @Test
    public void deleteCard() throws ParseException, BadRequestException {
        account.deleteCard(111111111L);
        assertEquals(0,account.getCardList().size());
    }

    @Test
    public void deleteNullCard() throws ParseException, BadRequestException {
        assertThrows(BadRequestException.class, () -> {
            account.deleteCard(null);
        });
    }

    @Test
    public void deleteNotExistingCard() throws ParseException, BadRequestException {
        assertThrows(BadRequestException.class, () -> {
            account.deleteCard(567L);
        });
    }

    @Test
    public void activateCardTest() throws BadRequestException {
        card.setActivated(false);
        account.activateCard(111111111L);
        assertEquals(true,card.getActivated());
    }
    @Test
    public void activateNullCardTest() throws BadRequestException {
        assertThrows(BadRequestException.class, () -> {
            account.activateCard(null);
        });
    }
    @Test
    public void activateActiveCardTest() throws BadRequestException {
        card.setActivated(true);
        assertThrows(BadRequestException.class, () -> {
            account.activateCard(1234567L);
        });
    }

    @Test
    public void activateNotExistingCardTest() throws BadRequestException {
        assertThrows(BadRequestException.class, () -> {
            account.activateCard(12345L);
        });
    }

    @Test
    public void blockCardTest() throws BadRequestException {
        card.setActivated(true);
        account.blockCard(111111111L);
        assertEquals(false,card.getActivated());
    }
    @Test
    public void blockNullCardTest() throws BadRequestException {
        assertThrows(BadRequestException.class, () -> {
            account.blockCard(null);
        });
    }
    @Test
    public void blockBlockedCardTest() throws BadRequestException {
        card.setActivated(false);
        assertThrows(RuntimeException.class, () -> {
            account.blockCard(1234567L);
        });
    }

    @Test
    public void blockNotExistingCardTest() throws BadRequestException {
        assertThrows(RuntimeException.class, () -> {
            account.blockCard(12345L);
        });
    }
}
