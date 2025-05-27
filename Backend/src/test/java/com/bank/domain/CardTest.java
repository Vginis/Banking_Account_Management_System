package com.bank.domain;

import com.bank.Initialization;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CardTest extends Initialization {

    /*get-set tests*/
    @Test
    public void getPinTest(){assertEquals("5555",card.getPin());}

    @Test
    public void getExpirationDateTest(){assertEquals(localDateTime,card.getExpirationDate());}

    @Test
    public void getActiveTest(){assertEquals(true,card.getActivated());}

    @Test
    public void getAccountTest(){assertEquals(2,card.getAccount().getAccountNumber());}

    @Test
    public void setPinTest(){
        card.setPin("8888");
        assertEquals("8888",card.getPin());
    }

    @Test
    public void setExpirationDateTest() throws ParseException {
        LocalDateTime localDateTime1 = LocalDateTime.now().plusYears(5);

        card.setExpirationDate(localDateTime1);
        assertEquals(localDateTime1,card.getExpirationDate());
    }

    @Test
    public void setActivatedTest(){
        card.setActivated(false);
        assertEquals(false, card.getActivated());
    }

    @Test
    public void setAccountTest(){
        Account account2 = new Account(4533,user,money, listCard,new ArrayList<>());
        card.setAccount(account2);
        assertEquals(4533,card.getAccount().getAccountNumber());
    }
}
