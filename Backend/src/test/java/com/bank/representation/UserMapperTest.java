package com.bank.representation;

import com.bank.Fixture;
import com.bank.Initialization;
import com.bank.domain.User;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest extends Initialization {
    @Inject
    UserMapper userMapper;

    @Transactional
    @Test
    public void testToRepresentation(){
        UserRepresentation userRepresentation = userMapper.userToRepresentation(user);
        assertEquals(userRepresentation.userId,user.getUserId());
        assertEquals(userRepresentation.email,user.getEmail());
        assertEquals(userRepresentation.firstName,user.getFirstName());
        assertEquals(userRepresentation.lastName, user.getLastName());
        assertEquals(userRepresentation.address,user.getAddress().getStreet()+ " " + user.getAddress().getNumber() + " " + user.getAddress().getZipCode());
        assertEquals(userRepresentation.accountList.size(), user.getAccountList().size());
    }

    @Transactional
    @Test
    public void testToModel() throws BadRequestException {
        UserRepresentation userRepresentation = fixture.createUserRepresentation();
        User user4 = userMapper.userRepresentationToModel(userRepresentation);
        assertEquals(user4.getUserId(), userRepresentation.userId);
        assertEquals(user4.getAddress().getStreet()+" "+user4.getAddress().getNumber()+" "
                +user4.getAddress().getZipCode(),userRepresentation.address);
        assertEquals(user4.getEmail(),userRepresentation.email);
        assertEquals(user4.getFirstName(),userRepresentation.firstName);
        assertEquals(user4.getLastName(), userRepresentation.lastName);
        assertEquals(user4.getAccountList().size(),userRepresentation.accountList.size());
    }
}
