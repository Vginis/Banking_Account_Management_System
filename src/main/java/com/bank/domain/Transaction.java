package com.bank.domain;

import com.bank.util.Money;
import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name="moneyTransactions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transactionType" , discriminatorType = DiscriminatorType.STRING)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @Column(name="transDate",nullable = false)
    private LocalDateTime date;
    @org.hibernate.annotations.Type(value=com.bank.repository.MoneyCustomType.class)
    @Column(name="amount",nullable = false)
    private Money amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="accountNumber")
    private Account account;

    public Transaction(Integer transactionId, LocalDateTime date, Money amount, Account account) {
        this.transactionId = transactionId;
        this.date = date;
        this.amount = amount;
        this.account = account;
    }

    public Transaction() {
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
