package com.bank.controller;

import com.bank.Fixture;
import com.bank.Initialization;
import com.bank.representation.AccountRepresentation;
import com.bank.representation.WithdrawalRepresentation;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static com.bank.util.Urls.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class WithdrawalControllerTest extends Initialization {
    @Test
    public void findAllWithdrawalTest(){
        List<WithdrawalRepresentation> withdrawals = when().get(WITHDRAWAL_URL)
                .then().statusCode(200).extract().as(new TypeRef<List<WithdrawalRepresentation>>() {});
        assertEquals(withdrawals.size(),3);
    }

    @Test
    public void findOneWithdrawalTest()  {
        WithdrawalRepresentation withdrawalRepresentation = when().get(WITHDRAWAL_URL + "/5").then()
                .statusCode(200).extract().as(WithdrawalRepresentation.class);
        assertEquals(withdrawalRepresentation.transactionId,5);
        assertEquals(withdrawalRepresentation.date, "2024-01-01 00:00:00");
    }

    @Test
    public void findOneNullWithdrawalTest(){
        Integer transactionId = null;
        when().get(WITHDRAWAL_URL + "/" +transactionId).then()
                .statusCode(400);
    }

    @Test
    public void findInvalidIdWithdrawalTest(){
        when().get(WITHDRAWAL_URL + "/88889").then()
                .statusCode(404);
    }

    @Test
    public void createWithdrawalTest(){
        WithdrawalRepresentation withdrawalRepresentation = Fixture.fixture.createWithdrawalRepresentation();
        given().contentType(ContentType.JSON)
                .body(withdrawalRepresentation)
                .when().post(WITHDRAWAL_URL +"/new").then().statusCode(201);
    }

    @Test
    public void createInvalidWithdrawalTest(){
        WithdrawalRepresentation withdrawalRepresentation = when().get(WITHDRAWAL_URL+"/5").then()
                .statusCode(200).extract().as(WithdrawalRepresentation.class);
        given().contentType(ContentType.JSON)
                .body(withdrawalRepresentation)
                .when().post(WITHDRAWAL_URL +"/new").then().statusCode(400);
    }
    @Test
    public void createWrongWithdrawalTest(){
        WithdrawalRepresentation withdrawalRepresentation = new WithdrawalRepresentation();
        withdrawalRepresentation.transactionId = 12;
        given().contentType(ContentType.JSON)
                .body(withdrawalRepresentation)
                .when().post(WITHDRAWAL_URL +"/new").then().statusCode(500);
    }
    @Test
    public void updateWithdrawalTest(){
        WithdrawalRepresentation withdrawalRepresentation = when().get(WITHDRAWAL_URL+"/5").then()
                .statusCode(200).extract().as(WithdrawalRepresentation.class);

        withdrawalRepresentation.account = 1;
        withdrawalRepresentation.date = "2029-01-01 00:00:00";

        given().contentType(ContentType.JSON).body(withdrawalRepresentation)
                .when().put(WITHDRAWAL_URL + "/update/5").then().statusCode(204);

        WithdrawalRepresentation updated = when().get(WITHDRAWAL_URL + "/5").then().statusCode(200).extract()
                .as(WithdrawalRepresentation.class);
        assertEquals("2029-01-01 00:00:00",updated.date);
        assertEquals(1,updated.account);
    }

    @Test
    public void updateInvalidTest(){
        Integer id = null;
        given().contentType(ContentType.JSON).body(fixture.createWithdrawalRepresentation())
                .when().put(WITHDRAWAL_URL + "/update/" + id).then().statusCode(400);

        given().contentType(ContentType.JSON).body(fixture.createDepositRepresentation())
                .when().put(WITHDRAWAL_URL + "/update/212").then().statusCode(404);
    }

    @Test
    public void deleteWithdrawalTest() {
        given().contentType(ContentType.JSON)
                .when().delete(WITHDRAWAL_URL + "/delete/6").then().statusCode(204);

        given().when().get(WITHDRAWAL_URL +"/6").then().statusCode(404);
    }

    @Test
    public void deleteWithdrawalNullTest(){
        Integer transactionId = null;
        given().contentType(ContentType.JSON)
                .when().delete(WITHDRAWAL_URL +"/delete/"+transactionId).then().statusCode(400);
    }

    @Test
    public void deleteCardInvalidTest(){
        given().contentType(ContentType.JSON)
                .when().delete(WITHDRAWAL_URL +"/delete/56789013").then().statusCode(404);
    }

    @Test
    public void makeWithdrawalTest(){
        AccountRepresentation accountRepresentation = when().get(ACCOUNT_URL+"/1").then()
                .statusCode(200).extract().as(AccountRepresentation.class);
        BigDecimal initial = new BigDecimal(accountRepresentation.balance);
        given().contentType(ContentType.JSON)
                .when().put(WITHDRAWAL_URL +"/make/1?amount=5").then().statusCode(204);
        AccountRepresentation accountRepresentation2 = when().get(ACCOUNT_URL+"/1").then()
                .statusCode(200).extract().as(AccountRepresentation.class);
        assertEquals(initial.subtract(new BigDecimal(5)),new BigDecimal(accountRepresentation2.balance));
    }

    @Test
    public void makeInvalidWithdrawalTest(){
        given().contentType(ContentType.JSON)
                .when().put(WITHDRAWAL_URL +"/make/231?amount=5")
                .then().statusCode(404);
        given().contentType(ContentType.JSON)
                .when().put(WITHDRAWAL_URL +"/make/1?amount=500000")
                .then().statusCode(400);
    }
}
