package com.bank.domain;

import com.bank.Initialization;
import com.bank.util.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.bank.util.Currency.EUR;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AddressTest extends Initialization {
    @Test
    public void getNumberTest(){
        assertEquals("13",address.getNumber());
    }

    @Test
    public void getZipCodeTest(){
        assertEquals("55555",address.getZipCode());
    }
}
