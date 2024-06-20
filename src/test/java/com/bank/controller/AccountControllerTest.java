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
    public void findAllAccountTest(){
        List<AccountRepresentation> accounts = when().get(ACCOUNT_URL)
                .then().statusCode(200).extract().as(new TypeRef<List<AccountRepresentation>>() {});
        assertEquals(accounts.size(),2);
    }

    @Test
    public void findOneAccountTest(){
        AccountRepresentation account = when().get(ACCOUNT_URL + "/1").then()
                .statusCode(200).extract().as(AccountRepresentation.class);
        assertEquals(account.accountNumber, 1);
        assertEquals(account.balance,45);
        assertEquals(account.userId,1);
    }

    @Test
    public void findOneNullAccountTest(){
        Integer accountId = null;
        when().get(ACCOUNT_URL + "/" +accountId).then()
                .statusCode(400);
    }

    @Test
    public void findInvalidIdAccountTest(){
        when().get(ACCOUNT_URL + "/88889").then()
                .statusCode(404);
    }

    @Test
    public void createAccountTest(){
        AccountRepresentation accountRepresentation = Fixture.fixture.createAccountRepresentation2();
        given().contentType(ContentType.JSON)
                .body(accountRepresentation)
                .when().post(ACCOUNT_URL +"/new").then().statusCode(201);
    }

    @Test
    public void createInvalidAccountTest(){
        AccountRepresentation account = fixture.createAccountRepresentation();
        given().contentType(ContentType.JSON)
                .body(account)
                .when().post(ACCOUNT_URL +"/new").then().statusCode(400);
    }
    @Test
    public void createWrongAccountTest(){
        AccountRepresentation account = new AccountRepresentation();
        account.accountNumber = 12;
        given().contentType(ContentType.JSON)
                .body(account)
                .when().post(ACCOUNT_URL +"/new").then().statusCode(500);
    }

    @Test
    @Transactional
    public void updateAccountTest(){
        AccountRepresentation accountRepresentation = when().get(ACCOUNT_URL+"/2").then()
                .statusCode(200).extract().as(AccountRepresentation.class);

        accountRepresentation.balance = 10000;
        accountRepresentation.userId = 2;

        given().contentType(ContentType.JSON).body(accountRepresentation)
                .when().put(ACCOUNT_URL + "/update/2").then().statusCode(204);

        AccountRepresentation updated = when().get(ACCOUNT_URL + "/2").then().statusCode(200).extract()
                .as(AccountRepresentation.class);
        assertEquals(10000,updated.balance);
        assertEquals(2,updated.userId);
    }

    @Test
    public void updateInvalidTest(){
        Integer id = null;
        given().contentType(ContentType.JSON).body(fixture.createAccountRepresentation())
                .when().put(ACCOUNT_URL + "/update/" + id).then().statusCode(400);

        given().contentType(ContentType.JSON).body(fixture.createAccountRepresentation())
                .when().put(ACCOUNT_URL + "/update/212").then().statusCode(404);
    }

    @Test
    public void deleteAccountTest() {
        given().contentType(ContentType.JSON)
                .when().delete(ACCOUNT_URL + "/delete/3").then().statusCode(204);

        given().when().get(ACCOUNT_URL +"/3").then().statusCode(404);
    }

    @Test
    public void deleteAccountNullTest(){
        Integer accountNumber = null;
        given().contentType(ContentType.JSON)
                .when().delete(ACCOUNT_URL +"/delete/"+accountNumber).then().statusCode(400);
    }

    @Test
    public void deleteCardInvalidTest(){
        given().contentType(ContentType.JSON)
                .when().delete(ACCOUNT_URL +"/delete/56789013").then().statusCode(404);
    }

    @Test
    public void addCardToListTest(){
        AccountRepresentation accountRepresentation = when().get(ACCOUNT_URL+"/2").then()
                .statusCode(200).extract().as(AccountRepresentation.class);

        given().contentType(ContentType.JSON)
                .when().post(ACCOUNT_URL +"/addCard/2?pin=1234&expirationDate=2030-11-26 00:00:00")
                .then().statusCode(200);
        assertEquals(accountRepresentation.cardList.size(),2);
    }

    @Test
    public void addNullCardToListTest(){
        Integer cardId = null;

        given().contentType(ContentType.JSON)
                .when().post(ACCOUNT_URL +"/addCard/" + cardId +"?pin=1234&expirationDate=2030-11-26 00:00:00")
                .then().statusCode(400);

        given().contentType(ContentType.JSON)
                .when().post(ACCOUNT_URL +"/addCard/2323?pin=1234&expirationDate=2030-11-26 00:00:00")
                .then().statusCode(404);

        given().contentType(ContentType.JSON)
                .when().post(ACCOUNT_URL +"/addCard/1?pin=1234&expirationDate=2030-11-26")
                .then().statusCode(500);

        given().contentType(ContentType.JSON)
                .when().post(ACCOUNT_URL +"/addCard/2?pin=1AA4&expirationDate=2030-11-26 00:00:00")
                .then().statusCode(400);
    }

    @Test
    public void deleteCardFromListTest(){
        AccountRepresentation accountRepresentation = when().get(ACCOUNT_URL+"/2").then()
                .statusCode(200).extract().as(AccountRepresentation.class);
        given().contentType(ContentType.JSON)
                .when().delete(ACCOUNT_URL +"/delete/card/2?cardID=123456789014")
                .then().statusCode(204);
    }

    @Test
    public void deleteInvalidCardFromListTest(){
        Integer cardId = null;
        given().contentType(ContentType.JSON)
                .when().delete(ACCOUNT_URL +"/delete/card/2?cardID=" + cardId)
                .then().statusCode(400);

        given().contentType(ContentType.JSON)
                .when().delete(ACCOUNT_URL +"/delete/card/2323?cardID=123456789016")
                .then().statusCode(404);

        given().contentType(ContentType.JSON)
                .when().delete(ACCOUNT_URL +"/delete/card/2?cardID=123")
                .then().statusCode(404);

    }

    @Test
    @Transactional
    public void activateCardFromListTestTest(){
        AccountRepresentation accountRepresentation = when().get(ACCOUNT_URL+"/2").then()
                .statusCode(200).extract().as(AccountRepresentation.class);

        given().contentType(ContentType.JSON).body(accountRepresentation)
                .when().put(ACCOUNT_URL + "/activate/card/2?cardID=123456789017").then().statusCode(204);

        CardRepresentation updated = when().get(CARD_URL + "/123456789017").then().statusCode(200).extract()
                .as(CardRepresentation.class);
        assertEquals("true",updated.activated);
    }

    @Test
    @Transactional
    public void deactivateCardFromListTestTest(){
        AccountRepresentation accountRepresentation = when().get(ACCOUNT_URL+"/1").then()
                .statusCode(200).extract().as(AccountRepresentation.class);

        given().contentType(ContentType.JSON).body(accountRepresentation)
                .when().put(ACCOUNT_URL + "/deactivate/card/1?cardID=123456789015").then().statusCode(204);

        CardRepresentation updated = when().get(CARD_URL + "/123456789015").then().statusCode(200).extract()
                .as(CardRepresentation.class);
        assertEquals("false",updated.activated);
    }

    @Test
    public void activateDeactivateInvalidCardFromListTest(){
        Integer cardId = null;
        given().contentType(ContentType.JSON)
                .when().put(ACCOUNT_URL +"/activate/card/2?cardID=" + cardId)
                .then().statusCode(400);

        given().contentType(ContentType.JSON)
                .when().put(ACCOUNT_URL +"/activate/card/2?cardID=123456789016")
                .then().statusCode(404);

        given().contentType(ContentType.JSON)
                .when().put(ACCOUNT_URL +"/activate/card/2?cardID=123")
                .then().statusCode(404);

    }
}
