package com.bank.domain;

import com.bank.util.Currency;
import com.bank.util.Money;
import jakarta.persistence.*;
import org.apache.coyote.BadRequestException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="accounts")
public class Account {
    @Id
    @Column(name="account_number")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    //@org.hibernate.annotations.Type(value=com.bank.repository.MoneyCustomType.class)
    @Column(name="balance",nullable = false)
    private Money balance;

    @OneToMany(fetch = FetchType.LAZY,orphanRemoval = true,mappedBy = "account")
    private List<Card> cardList;

    @OneToMany(fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL,mappedBy = "account")
    private List<Transaction> transactionList;

    public Account(Integer accountNumber, User user, Money balance, List<Card> cardList, List<Transaction> transactionList) {
        this.accountNumber = accountNumber;
        this.user = user;
        this.balance = balance;
        this.cardList = cardList;
        this.transactionList = transactionList;
    }

    public Account() {
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public User getUser() {
        return user;
    }
    public Integer getUserId() {
        return user.getUserId();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public void addCard(Long cardId, String pin, LocalDateTime expirationDate, Boolean activated) throws BadRequestException {
        if(cardId == null || pin == null || expirationDate == null || activated == null){
            throw new BadRequestException("Don't send null values");
        }
        for (Card c : cardList)
            if (Objects.equals(c.getCardId(), cardId)){
                throw new BadRequestException("Card already with that id");
            }
        if(expirationDate.isBefore(LocalDateTime.now())){
            throw new BadRequestException("Invalid expiration Date!It is expired");
        }
        if(pin.matches("\\d{4}")){
            Card card = new Card(cardId,pin,expirationDate,activated,this);
            this.cardList.add(card);}
        else{
            throw new BadRequestException("Invalid Pin. Give a 4-digit number only");
        }
    }

    public void deleteCard(Long cardId) throws BadRequestException {
        if(cardId == null){throw new BadRequestException("Don't send null values");}
        for (Card c : cardList)
            if (c.getCardId().equals(cardId)){
                cardList.remove(c);
                return;
            }
        throw new BadRequestException("Card not found");
    }

    public void activateCard(Long cardId) throws BadRequestException {
        if(cardId == null){throw new BadRequestException("Don't send null values");}
        boolean act = false;
        boolean found = false;
        for (Card c : cardList) {
            if (c.getCardId().equals(cardId) && !c.getActivated()) {
                c.setActivated(true);
                act = true;
                found =true;
            }
        }
        if(!found){
            throw new BadRequestException("Card not found");
        }
        if(!act){
            throw new BadRequestException("Card already active");
        }

    }

    public void blockCard(Long cardId) throws BadRequestException,RuntimeException {
        if(cardId == null){throw new BadRequestException("Don't send null values");}
        for (Card c : cardList)
            if (c.getCardId().equals(cardId)){
                if(c.getActivated()){c.setActivated(false);}
                else {throw new BadRequestException("Card already inactive");}
                return;
            }
        throw new RuntimeException("Card not found");
    }

    public void transferFunds(Account to, Money money ) throws BadRequestException {
        if(to==null || money==null){
            throw new BadRequestException("Don't send null values");
        }
        if(this.getBalance().getAmount().compareTo(money.getAmount())<0){
            throw new BadRequestException("Insufficient Funds");
        }
        BigDecimal initial1 = this.getBalance().getAmount();
        BigDecimal initial2 = to.getBalance().getAmount();
        this.setBalance(new Money(initial1.subtract(money.getAmount()), Currency.EUR));
        to.setBalance(new Money(initial2.add(money.getAmount()), Currency.EUR));
    }
}
