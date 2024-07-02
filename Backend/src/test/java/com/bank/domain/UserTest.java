package com.bank.domain;


import com.bank.Initialization;
import com.bank.util.Money;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.bank.util.Currency.EUR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserTest extends Initialization {

    /*Getter-Setter tests*/
    @Test
    public void getUserIdTest(){assertEquals(user.getUserId(), 1000);}

    @Test
    public void getEmailTest(){assertEquals(user.getEmail(),"fotio7@gmail.com");}

    @Test
    public void getFullNameTest(){assertEquals(user.getFirstName() + " " + user.getLastName(),"Fotis Ioannidis");}

    @Test
    public void getAddressTest(){assertEquals("ApostolosNikolaidis",user.getAddress().getStreet());}

    @Test
    public void getAccountListTest(){assertEquals(1,user.getAccountList().size());}

    @Test
    public void setEmailTest() throws BadRequestException {
        user.setEmail("Fotis@gmail.com");
        assertEquals(user.getEmail(),"Fotis@gmail.com");
    }

    @Test
    public void setInvalidEmailTest() throws BadRequestException {
        assertThrows(BadRequestException.class, () -> {
            user.setEmail("Fotis@email.gr");
        });
    }

    @Test
    public void setFirstNameTest(){
        user.setFirstName("Fotaras");
        assertEquals(user.getFirstName(),"Fotaras");
    }

    @Test
    public void setLastNameTest(){
        user.setLastName("Fotaras");
        assertEquals(user.getLastName(),"Fotaras");
    }

    @Test
    public void setAddressTest(){
        address.setStreet("Votanikos");
        user.setAddress(address);
        assertEquals("Votanikos",user.getAddress().getStreet());
    }

    @Test
    public void setAccountListTest(){
        user.setAccountList(new ArrayList<>());
        assertEquals(0, user.getAccountList().size());
    }

    @Test
    public void setPasswordTest() throws BadRequestException {
        user.setPassword("asd@DFEas12");
        assertEquals(user.getPassword(),"asd@DFEas12");
    }


    /*domain-logic tests*/
    @Test
    public void addAccountTest() throws BadRequestException {
        user.addAccount(45,user,money);
        assertEquals(2,user.getAccountList().size());
    }

    @Test
    public void addNullAccountTest() throws BadRequestException {
        assertThrows(BadRequestException.class, () -> {
            user.addAccount(45,user,null);
        });
    }

    @Test
    public void addInvalidUserTest() throws BadRequestException {
        User user2 = new User(3333,"Kostas","Sloukas","kosslou@gmail.com", address, new ArrayList<>());
        assertThrows(BadRequestException.class, () -> {
            user.addAccount(45,user2,null);
        });
    }

    @Test
    public void deleteAccountTest() throws BadRequestException {
        user.deleteAccount(account);
        assertEquals(0,user.getAccountList().size());
    }

    @Test
    public void deleteNullAccountTest(){
        assertThrows(BadRequestException.class, () -> {
            user.deleteAccount(null);
        });
    }

    @Test
    public void deleteInvalidAccountTest(){
        Account account2 = new Account(56,user,money, new ArrayList<>(),new ArrayList<>());
        assertThrows(BadRequestException.class, () -> {
            user.deleteAccount(account2);
        });
    }
}
