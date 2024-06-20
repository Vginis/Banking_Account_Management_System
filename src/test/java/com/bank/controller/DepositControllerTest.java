package com.bank.controller;

import com.bank.Fixture;
import com.bank.Initialization;
import com.bank.representation.AccountRepresentation;
import com.bank.representation.DepositRepresentation;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.bank.util.Urls.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DepositControllerTest extends Initialization {


    @Test
    public void findAllDepositTest(){
        List<DepositRepresentation> deposits = when().get(DEPOSIT_URL)
                .then().statusCode(200).extract().as(new TypeRef<List<DepositRepresentation>>() {});
        assertEquals(deposits.size(),2);
    }

    @Test
    public void findOneDepositTest()  {
        DepositRepresentation depositRepresentation = when().get(DEPOSIT_URL + "/4").then()
                .statusCode(200).extract().as(DepositRepresentation.class);
        assertEquals(depositRepresentation.transactionId,4);
        assertEquals(depositRepresentation.date, "2025-01-01 00:00:00");
    }

    @Test
    public void findOneNullDepositTest(){
        Integer transactionId = null;
        when().get(DEPOSIT_URL + "/" +transactionId).then()
                .statusCode(400);
    }

    @Test
    public void findInvalidIdDepositTest(){
        when().get(DEPOSIT_URL + "/88889").then()
                .statusCode(404);
    }

    @Test
    public void createDepositTest(){
        DepositRepresentation depositRepresentation = Fixture.fixture.createDepositRepresentation();
        given().contentType(ContentType.JSON)
                .body(depositRepresentation)
                .when().post(DEPOSIT_URL +"/new").then().statusCode(201);
    }

    @Test
    public void createInvalidDepositTest(){
        DepositRepresentation depositRepresentation = when().get(DEPOSIT_URL+"/3").then()
                .statusCode(200).extract().as(DepositRepresentation.class);
        given().contentType(ContentType.JSON)
                .body(depositRepresentation)
                .when().post(DEPOSIT_URL +"/new").then().statusCode(400);
    }
    @Test
    public void createWrongDepositTest(){
        DepositRepresentation depositRepresentation = new DepositRepresentation();
        depositRepresentation.transactionId = 12;
        given().contentType(ContentType.JSON)
                .body(depositRepresentation)
                .when().post(DEPOSIT_URL +"/new").then().statusCode(500);
    }

    @Test
    @Transactional
    public void updateDepositTest(){
        DepositRepresentation depositRepresentation = when().get(DEPOSIT_URL+"/3").then()
                .statusCode(200).extract().as(DepositRepresentation.class);

        depositRepresentation.account = 2;
        depositRepresentation.date = "2029-01-01 00:00:00";

        given().contentType(ContentType.JSON).body(depositRepresentation)
                .when().put(DEPOSIT_URL + "/update/3").then().statusCode(204);

        DepositRepresentation updated = when().get(DEPOSIT_URL + "/3").then().statusCode(200).extract()
                .as(DepositRepresentation.class);
        assertEquals("2029-01-01 00:00:00",updated.date);
        assertEquals(2,updated.account);
    }

    @Test
    public void updateInvalidTest(){
        Integer id = null;
        given().contentType(ContentType.JSON).body(fixture.createDepositRepresentation())
                .when().put(DEPOSIT_URL + "/update/" + id).then().statusCode(400);

        given().contentType(ContentType.JSON).body(fixture.createDepositRepresentation())
                .when().put(DEPOSIT_URL + "/update/212").then().statusCode(404);
    }

    @Test
    public void deleteDepositTest() {
        given().contentType(ContentType.JSON)
                .when().delete(DEPOSIT_URL + "/delete/3").then().statusCode(204);

        given().when().get(DEPOSIT_URL +"/3").then().statusCode(404);
    }

    @Test
    public void deleteDepositNullTest(){
        Integer transactionId = null;
        given().contentType(ContentType.JSON)
                .when().delete(DEPOSIT_URL +"/delete/"+transactionId).then().statusCode(400);
    }

    @Test
    public void deleteCardInvalidTest(){
        given().contentType(ContentType.JSON)
                .when().delete(DEPOSIT_URL +"/delete/56789013").then().statusCode(404);
    }

    @Test
    public void makeDepositTest(){
        AccountRepresentation accountRepresentation = when().get(ACCOUNT_URL+"/2").then()
                .statusCode(200).extract().as(AccountRepresentation.class);
        BigDecimal initial = new BigDecimal(accountRepresentation.balance);
        given().contentType(ContentType.JSON)
                .when().put(DEPOSIT_URL +"/make/2?amount=5000").then().statusCode(204);
        AccountRepresentation accountRepresentation2 = when().get(ACCOUNT_URL+"/2").then()
                .statusCode(200).extract().as(AccountRepresentation.class);
        assertEquals(initial.add(new BigDecimal(5000)),new BigDecimal(accountRepresentation2.balance));
    }

    @Test
    public void makeInvalidDepositTest(){
        given().contentType(ContentType.JSON)
                .when().put(DEPOSIT_URL +"/make/231?amount=5000")
                .then().statusCode(404);
    }

}
