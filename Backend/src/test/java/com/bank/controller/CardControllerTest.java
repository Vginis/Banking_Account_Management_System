package com.bank.controller;

import com.bank.Fixture;
import com.bank.Initialization;
import com.bank.representation.CardRepresentation;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static com.bank.util.Urls.CARD_URL;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CardControllerTest extends Initialization {

    @Autowired
    MockMvc mockMvc;


    @Test
    public void findAllCard() {
        List<CardRepresentation> cards = given().header("Authorization", "Bearer " + token)
                .when().get(CARD_URL)
                .then().statusCode(200).extract().as(new TypeRef<List<CardRepresentation>>() {});
        assertEquals(5, cards.size());
    }

    @Test
    public void findCardByIdTest() throws Exception {
        Long cardId = 123456789012L;
        ResultActions resultActions = mockMvc.perform(get(CARD_URL + "/{id}", cardId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cardId").value(cardId))
                .andExpect(jsonPath("$.pin").value("0000"))
                .andExpect(jsonPath("$.accountNumber").value(1));
    }

    @Test
    public void findCardsByAccountTest() {
        List<CardRepresentation> cards = given().header("Authorization", "Bearer " + token)
                .pathParam("id", 1)
                .when().get(CARD_URL + "/account/{id}")
                .then().statusCode(200).extract().as(new TypeRef<List<CardRepresentation>>() {});
        assertEquals(4, cards.size());
    }

    @Test
    public void createCardTest() {
        CardRepresentation cardRepresentation = Fixture.fixture.createCardRepresentation2();
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(cardRepresentation)
                .when().post(CARD_URL + "/new")
                .then().statusCode(201);
    }

    @Test
    public void createNullCardTest() {
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON).body(Optional.empty())
                .when().post(CARD_URL + "/new")
                .then().statusCode(400);
    }

    @Test
    public void createInvalidCardTest() {
        CardRepresentation cardRepresentation = new CardRepresentation();
        cardRepresentation.cardId = 123456789012L;
        cardRepresentation.pin = "0000";

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON).body(cardRepresentation)
                .when().post(CARD_URL + "/new")
                .then().statusCode(400);
    }

    @Test
    public void updateCardTest() {
        CardRepresentation cardRepresentation = given().header("Authorization", "Bearer " + token)
                .when().get(CARD_URL + "/123456789013")
                .then().statusCode(200).extract().as(CardRepresentation.class);

        cardRepresentation.pin = "3333";
        cardRepresentation.date = "2027-12-26 00:00:00";

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON).body(cardRepresentation)
                .when().put(CARD_URL + "/update/123456789013")
                .then().statusCode(204);

        CardRepresentation updated = given().header("Authorization", "Bearer " + token)
                .when().get(CARD_URL + "/123456789013")
                .then().statusCode(200).extract().as(CardRepresentation.class);

        assertEquals("3333", updated.pin);
        assertEquals("2027-12-26 00:00:00", updated.date);
    }

    @Test
    public void updateNullCardTest() {
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON).body(Optional.empty())
                .when().put(CARD_URL + "/update/123456789013")
                .then().statusCode(400);
    }

    @Test
    public void updateInvalidCardIdTest() {
        CardRepresentation cardRepresentation = fixture.createCardRepresentation();
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON).body(cardRepresentation)
                .when().put(CARD_URL + "/update/56789013")
                .then().statusCode(404);
    }

    @Test
    public void deleteCardTest() {
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when().delete(CARD_URL + "/delete/123456789016")
                .then().statusCode(204);

        given().when().get(CARD_URL + "/123456789016")
                .then().statusCode(404);
    }

    @Test
    public void deleteCardNullTest() {
        Long cardId = null;
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when().delete(CARD_URL + "/delete/" + cardId)
                .then().statusCode(400);
    }

    @Test
    public void deleteCardInvalidTest() {
        CardRepresentation cardRepresentation = fixture.createCardRepresentation();
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON).body(cardRepresentation)
                .when().delete(CARD_URL + "/delete/56789013")
                .then().statusCode(404);
    }
}