package com.bank.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name="cards")
public class Card {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="cardId", length=12)
    private Long cardId;
    @Column(name="pin",nullable = false,length = 4)
    private String pin;
    @Column(name="expiration",nullable = false)
    private LocalDateTime expirationDate;
    @Column(name="activated", nullable = false)
    private Boolean activated;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="accountNumber")
    private Account account;

    public Card() {
    }

    public Card(Long cardId, String pin, LocalDateTime expirationDate, Boolean activated, Account account) {
        this.cardId = cardId;
        this.pin = pin;
        this.expirationDate = expirationDate;
        this.activated = activated;
        this.account = account;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getCardId() {
        return cardId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

