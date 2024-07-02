package com.bank.controller;

import com.bank.Initialization;
import com.bank.representation.UserRepresentation;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


import static com.bank.util.Urls.USER_URL;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserControllerTest extends Initialization {

    @Test
    public void findAllCard() {
        List<UserRepresentation> users = given().header("Authorization", "Bearer " + token)
                .when().get(USER_URL)
                .then().statusCode(200).extract().as(new TypeRef<List<UserRepresentation>>() {});
        assertEquals(3, users.size());
    }

    @Test
    public void findOneUserTest() {
        UserRepresentation user = given().header("Authorization", "Bearer " + token)
                .when().get(USER_URL + "/1")
                .then().statusCode(200).extract().as(UserRepresentation.class);
        assertEquals(1, user.userId);
        assertEquals("Cisse", user.lastName);
        assertEquals("Patission 3 12321", user.address);
    }

    @Test
    public void findOneNullUserTest() {
        Integer userId = null;
        given().header("Authorization", "Bearer " + token)
                .when().get(USER_URL + "/" + userId)
                .then().statusCode(400);
    }

    @Test
    public void findInvalidIdUserTest() {
        given().header("Authorization", "Bearer " + token)
                .when().get(USER_URL + "/88889")
                .then().statusCode(404);
    }

    @Test
    public void createUserTest() {
        UserRepresentation userRepresentation = fixture.createUserRepresentation();
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(userRepresentation)
                .when().post(USER_URL + "/new/passwEord123!")
                .then().statusCode(201);
    }

    @Test
    public void createInvalidAccountTest() {
        UserRepresentation user = given().header("Authorization", "Bearer " + token).get(USER_URL + "/1").then()
                .statusCode(200).extract().as(UserRepresentation.class);
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(user)
                .when().post(USER_URL + "/new/passWord123@")
                .then().statusCode(400);
    }

    @Test
    public void createWrongUserTest() {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.userId = 4;
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(userRepresentation)
                .when().post(USER_URL + "/new/passWord123@")
                .then().statusCode(500);
    }

    @Test
    public void createWrongPasswordUserTest() {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.userId = 4;
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(userRepresentation)
                .when().post(USER_URL + "/new/password")
                .then().statusCode(500);
    }

    @Test
    @Transactional
    public void updateUserTest() {
        UserRepresentation userRepresentation = given().header("Authorization", "Bearer " + token)
                .when().get(USER_URL + "/3")
                .then().statusCode(200).extract().as(UserRepresentation.class);

        userRepresentation.firstName = "Petros";
        userRepresentation.lastName = "Mantalos";

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON).body(userRepresentation)
                .when().put(USER_URL + "/update/3")
                .then().statusCode(204);

        UserRepresentation updated = given().header("Authorization", "Bearer " + token)
                .when().get(USER_URL + "/3")
                .then().statusCode(200).extract().as(UserRepresentation.class);
        assertEquals("Petros", updated.firstName);
        assertEquals("Mantalos", updated.lastName);
    }

    @Test
    public void updateInvalidTest() {
        Integer id = null;
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON).body(fixture.createUserRepresentation())
                .when().put(USER_URL + "/update/" + id)
                .then().statusCode(400);

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON).body(fixture.createAccountRepresentation())
                .when().put(USER_URL + "/update/212")
                .then().statusCode(404);
    }

    @Test
    public void deleteUserTest() {
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when().delete(USER_URL + "/delete/3")
                .then().statusCode(204);

        given().header("Authorization", "Bearer " + token)
                .when().get(USER_URL + "/3")
                .then().statusCode(404);
    }

    @Test
    public void deleteUserNullTest() {
        Integer userId = null;
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when().delete(USER_URL + "/delete/" + userId)
                .then().statusCode(400);
    }

    @Test
    public void deleteCardInvalidTest() {
        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when().delete(USER_URL + "/delete/56789013")
                .then().statusCode(404);
    }
}

