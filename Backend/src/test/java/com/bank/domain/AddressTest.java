package com.bank.domain;

import com.bank.Initialization;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
