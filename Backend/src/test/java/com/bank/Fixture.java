package com.bank;

import com.bank.domain.Deposit;
import com.bank.representation.*;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Fixture extends Initialization{

    public final static String API_ROOT  = "http://localhost:8081/";

    public CardRepresentation createCardRepresentation(){
        CardRepresentation cardRepresentation = new CardRepresentation();
        cardRepresentation.cardId = 123456789012L;
        cardRepresentation.pin = "0000";
        cardRepresentation.date = "2027-11-26 00:00:00";
        cardRepresentation.activated = "true";
        cardRepresentation.accountNumber = 1;
        return cardRepresentation;
    }

    public CardRepresentation createCardRepresentation2(){
        CardRepresentation cardRepresentation = new CardRepresentation();
        cardRepresentation.cardId = 123456789019L;
        cardRepresentation.pin = "0000";
        cardRepresentation.date = "2027-11-26 00:00:00";
        cardRepresentation.activated = "true";
        cardRepresentation.accountNumber = 1;
        return cardRepresentation;
    }

    public CardRepresentation createCardRepresentation3(){
        CardRepresentation cardRepresentation = new CardRepresentation();
        cardRepresentation.cardId = 123456789019L;
        cardRepresentation.pin = "0000";
        cardRepresentation.date = "2027-11-26 00:00:00";
        cardRepresentation.activated = "true";
        cardRepresentation.accountNumber = 1;
        return cardRepresentation;
    }

    public AccountRepresentation createAccountRepresentation(){
        AccountRepresentation accountRepresentation = new AccountRepresentation();
        accountRepresentation.accountNumber = 1;
        accountRepresentation.balance = 45;
        accountRepresentation.userId = 1;
        accountRepresentation.cardList = new ArrayList<>();
        accountRepresentation.cardList.add(111111111L);
        accountRepresentation.transactionList = new ArrayList<>();
        accountRepresentation.transactionList.add(1001);
        accountRepresentation.transactionList.add(1002);
        return accountRepresentation;
    }

    public AccountRepresentation createAccountRepresentation2(){
        AccountRepresentation accountRepresentation = new AccountRepresentation();
        accountRepresentation.accountNumber = 7;
        accountRepresentation.balance = 45;
        accountRepresentation.userId = 1;
        accountRepresentation.cardList = new ArrayList<>();
        accountRepresentation.transactionList = new ArrayList<>();
        return accountRepresentation;
    }

    public UserRepresentation createUserRepresentation(){
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.userId = 1000;
        userRepresentation.address = "ApostolosNikolaidis 13 55555";
        userRepresentation.email = "fotio7@gmail.com";
        userRepresentation.username = "fotio7";
        userRepresentation.isAdmin = false;
        userRepresentation.firstName = "Fotis";
        userRepresentation.lastName = "Ioannidis";
        userRepresentation.accountList = new ArrayList<>();
        userRepresentation.accountList.add(1);
        return userRepresentation;
    }

    public WithdrawalRepresentation createWithdrawalRepresentation(){
        WithdrawalRepresentation transactionRepresentation = new WithdrawalRepresentation();
        transactionRepresentation.transactionId = 1002;
        transactionRepresentation.date = "2030-11-26 00:00:00";
        transactionRepresentation.amount = new BigDecimal(45);
        transactionRepresentation.account = 2;
        return transactionRepresentation;
    }
    public DepositRepresentation createDepositRepresentation(){
        DepositRepresentation transactionRepresentation = new DepositRepresentation();
        transactionRepresentation.transactionId = 1002;
        transactionRepresentation.date = "2030-11-26 00:00:00";
        transactionRepresentation.amount = new BigDecimal(45);
        transactionRepresentation.account = 2;
        return transactionRepresentation;
    }
}
