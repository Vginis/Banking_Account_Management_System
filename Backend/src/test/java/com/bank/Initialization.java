package com.bank;

import com.bank.Fixture;
import com.bank.domain.*;
import com.bank.util.Money;
import com.bank.util.Currency;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.bank.util.Currency.EUR;
import static io.restassured.RestAssured.given;

public abstract class Initialization {

    protected static String token;
    protected static Fixture fixture;
    protected Account account;
    protected User user;
    protected List<Card> listCard;
    protected List<Account> accountList;
    protected List<Transaction> transactionList;
    protected Card card;
    protected LocalDateTime localDateTime;
    protected DateTimeFormatter dateTimeFormatter;
    protected Address address;
    protected Money money;
    protected Deposit deposit;
    protected Withdrawal withdrawal;


    @BeforeAll
    public static void globalSetup() {
        fixture = new Fixture();
    }

    @BeforeEach
    public void setup() throws ParseException {
        RestAssured.baseURI = "http://localhost:8094"; // Replace with your base URI
        token = obtainAuthToken();

        money = new Money(new BigDecimal(45),EUR);
        address = new Address();
        address.setNumber("13");
        address.setStreet("ApostolosNikolaidis");
        address.setZipCode("55555");

        accountList = new ArrayList<Account>();
        listCard = new ArrayList<Card>();

        user = new User(1000, "Fotis", "Ioannidis", "fotio7@gmail.com", address, new ArrayList<>());
        account = new Account(2,user,money,listCard, new ArrayList<>());


        String dateTimeString = "2030-11-26 00:00:00";


        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        localDateTime = LocalDateTime.parse(dateTimeString, dateTimeFormatter);

        card = new Card(111111111L, "5555", localDateTime, true, account);
        card.setExpirationDate(localDateTime);

        accountList.add(account);
        listCard.add(card);
        account.setCardList(listCard);

        deposit = new Deposit(1001,localDateTime, money,account);
        withdrawal = new Withdrawal(1002,localDateTime, money,account);
        transactionList = new ArrayList<>();
        transactionList.add(deposit);
        transactionList.add(withdrawal);

        user.setAccountList(accountList);
        account.setTransactionList(transactionList);
    }

    private static String obtainAuthToken() {
        // Implement this method to obtain JWT token, e.g., via login endpoint
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"djibril09\",\"password\":\"djibril09D!\"}")
                .post("/auth/login");

        return response.jsonPath().getString("token"); // Adjust based on your token structure
    }
}
