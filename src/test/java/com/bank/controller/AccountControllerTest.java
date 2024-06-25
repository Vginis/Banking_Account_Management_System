package com.bank.controller;

import com.bank.Fixture;
import com.bank.Initialization;
import com.bank.representation.AccountRepresentation;


import com.bank.representation.CardRepresentation;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bank.util.Urls.ACCOUNT_URL;
import static com.bank.util.Urls.CARD_URL;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AccountControllerTest extends Initialization {


    @Test
    public void findAllAccountTest() {
        List<AccountRepresentation> accounts = given().header("Authorization", "Bearer " + token)
                .when().get(ACCOUNT_URL)
                .then().statusCode(200).extract().as(new TypeRef<List<AccountRepresentation>>() {});
        assertEquals(3, accounts.size());
    }

    @Test
    public void findOneAccountTest() {
        AccountRepresentation account = given().header("Authorization", "Bearer " + token)
                .when().get(ACCOUNT_URL + "/1")
                .then().statusCode(200).extract().as(AccountRepresentation.class);
        assertEquals(1, account.accountNumber);
        assertEquals(45, account.balance);
        assertEquals(1, account.userId);
    }

    @Test
    public void createAccountTest() {
        AccountRepresentation accountRepresentation = Fixture.fixture.createAccountRepresentation2();
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(accountRepresentation)
                .when().post(ACCOUNT_URL + "/new")
                .then().statusCode(201);
    }

    @Test
    public void updateAccountTest() {
        AccountRepresentation accountRepresentation = given().header("Authorization", "Bearer " + token)
                .when().get(ACCOUNT_URL + "/2")
                .then().statusCode(200).extract().as(AccountRepresentation.class);

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(accountRepresentation)
                .when().put(ACCOUNT_URL + "/update/2")
                .then().statusCode(204);
    }



}

