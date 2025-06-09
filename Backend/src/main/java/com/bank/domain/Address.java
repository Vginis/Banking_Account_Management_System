package com.bank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    @Column(name="street")
    private String street;
    @Column(name="number")
    private String number;
    @Column(name="zip_code")
    private String zipCode;

    public Address() {
    }

    public Address(String addressString) {
        String[] wordList = new String[3];
        wordList = addressString.split(" ");
        this.street = wordList[0];
        this.number = wordList[1];
        this.zipCode = wordList[2];
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}

