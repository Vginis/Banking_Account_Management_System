package com.bank.representation;

import com.bank.Fixture;
import com.bank.Initialization;
import com.bank.domain.Account;
import com.bank.domain.Address;
import com.bank.domain.User;
import com.bank.repository.AccountRepository;
import com.bank.util.Currency;
import com.bank.util.Money;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AccountMapperTest extends Initialization {

    @Inject
    AccountMapper accountMapper;

    @Transactional
    @Test
    public void testToRepresentation(){
        user = new User(1000,"Fotis","Ioannidis","fotio7@gmail.com", new Address(), new ArrayList<>());
        account = new Account(2,user,new Money(new BigDecimal(45), Currency.EUR), new ArrayList<>(),new ArrayList<>());
        AccountRepresentation accountRepresentation = accountMapper.accountToRepresentation(account);
        assertEquals(account.getAccountNumber(), accountRepresentation.accountNumber);
        assertEquals(account.getUserId(),accountRepresentation.userId);
        assertEquals(account.getCardList().size(),accountRepresentation.cardList.size());
        assertEquals(account.getBalance().getAmount(), new BigDecimal(accountRepresentation.balance));
    }

    @Transactional
    @Test
    public void testToModel(){
        Fixture fixture = new Fixture();
        AccountRepresentation accountRepresentation = fixture.createAccountRepresentation();
        Account account3 = accountMapper.accountRepresentationToModel(accountRepresentation);
        assertEquals(account3.getAccountNumber(),accountRepresentation.accountNumber);
        assertEquals(account3.getBalance().getAmount(),new BigDecimal(accountRepresentation.balance));
        assertEquals(account3.getUser().getUserId(), accountRepresentation.userId);
        assertEquals(account3.getCardList().size(), accountRepresentation.cardList.size());
    }
}
