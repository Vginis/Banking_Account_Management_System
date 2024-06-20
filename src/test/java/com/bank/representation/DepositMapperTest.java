package com.bank.representation;

import com.bank.Initialization;
import com.bank.domain.Deposit;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DepositMapperTest extends Initialization {
    @Inject
    DepositMapper depositMapper;

    @Test
    public void testToRepresentation(){
        DepositRepresentation depositRepresentation = depositMapper.toRepresentation(deposit);
        assertEquals(depositRepresentation.account,deposit.getAccount().getAccountNumber());
        assertEquals(depositRepresentation.transactionId,deposit.getTransactionId());
        assertEquals(depositRepresentation.amount, deposit.getAmount().getAmount());
        assertEquals(depositRepresentation.date,"2030-11-26 00:00:00");
    }

    @Test
    public void testToModel() throws ParseException {
        DepositRepresentation depositRepresentation = fixture.createDepositRepresentation();
        Deposit deposit1 = depositMapper.toModel(depositRepresentation);
        assertEquals(deposit1.getAccount().getAccountNumber(), depositRepresentation.account);
        assertEquals(deposit1.getAmount().getAmount(), depositRepresentation.amount);
        assertEquals(deposit1.getTransactionId(), depositRepresentation.transactionId);
    }
}
