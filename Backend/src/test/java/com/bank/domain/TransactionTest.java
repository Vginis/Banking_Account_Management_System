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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import static com.bank.util.Currency.EUR;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionTest extends Initialization {

    /*Getter-Setter tests*/
    @Test
    public void getTransactionIdTest(){assertEquals(1001,deposit.getTransactionId());}

    @Test
    public void getDateTest(){assertEquals(deposit.getDate().toString(),"2030-11-26T00:00");}

    @Test
    public void getAmountTest(){assertEquals(deposit.getAmount().getAmount(),new BigDecimal(45));}

    @Test
    public void getAccountTest(){assertEquals(deposit.getAccount().getAccountNumber(),2);}

    @Test
    public void setTransactionDateTest() throws ParseException {
        deposit.setDate(localDateTime.plusYears(4));
        assertEquals(deposit.getDate().toString(),"2034-11-26T00:00");
    }

    @Test
    public void setAmountTest(){
        Money money = new Money(new BigDecimal(47),EUR);
        deposit.setAmount(new Money(new BigDecimal(47),EUR));
        assertEquals(deposit.getAmount().getAmount(),new BigDecimal(47));
    }

    @Test
    public void setAccountTest(){
        Account account = new Account(4,user,money, new ArrayList<>(),new ArrayList<>());
        deposit.setAccount(account);
        assertEquals(4,deposit.getAccount().getAccountNumber());
    }

    /*domain tests*/
    @Test
    public void makeDepositTest(){
        deposit.makeDeposit(new BigDecimal(50),account);
        assertEquals(new BigDecimal(95),account.getBalance().getAmount());
    }

    @Test
    public void makeInvalidDepositTest(){
        assertFalse(deposit.makeDeposit(new BigDecimal(50),null));
    }

    @Test
    public void makeWithdrawalTest() throws BadRequestException {
        withdrawal.makeWithdrawal(new BigDecimal(34),account);
        assertEquals(new BigDecimal(11),account.getBalance().getAmount());
    }

    @Test
    public void makeWithdrawalInsufficientFundTest(){
        assertThrows(BadRequestException.class, () -> {
            withdrawal.makeWithdrawal(new BigDecimal(340),account);;
        });
    }
    @Test
    public void makeInvalidWithdrawalTest() throws BadRequestException {
        assertFalse(withdrawal.makeWithdrawal(new BigDecimal(50),null));
    }
}
