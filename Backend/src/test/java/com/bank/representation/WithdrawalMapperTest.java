package com.bank.representation;

import com.bank.Initialization;
import com.bank.domain.Transaction;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class WithdrawalMapperTest extends Initialization {

    @Inject
    WithdrawalMapper withdrawalMapper;

    @Transactional
    @Test
    public void testToRepresentation(){
        WithdrawalRepresentation transactionRepresentation = withdrawalMapper.toRepresentation(withdrawal);
        assertEquals(transactionRepresentation.transactionId,withdrawal.getTransactionId());
        assertEquals(transactionRepresentation.amount,withdrawal.getAmount().getAmount());
        assertEquals(transactionRepresentation.date,"2030-11-26 00:00:00");
        assertEquals(transactionRepresentation.account, withdrawal.getAccount().getAccountNumber());
    }

    @Transactional
    @Test
    public void testToModel() throws ParseException {
        WithdrawalRepresentation transactionRepresentation = fixture.createWithdrawalRepresentation();
        Transaction transaction23 = withdrawalMapper.toModel(transactionRepresentation);
        assertEquals(transaction23.getTransactionId(), transactionRepresentation.transactionId);
        assertEquals(transaction23.getAccount().getAccountNumber(),transactionRepresentation.account);
        assertEquals(transaction23.getAmount().getAmount(), transactionRepresentation.amount);
    }
}
