package com.bank.domain;

import com.bank.util.Money;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.apache.coyote.BadRequestException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Calendar;

import static com.bank.util.Currency.EUR;

@Entity
@DiscriminatorValue("WITHDRAWAL")
public class Withdrawal extends Transaction{

    public Withdrawal(Integer transactionId, LocalDateTime date, Money amount, Account account) {
        super(transactionId, date, amount, account);
    }

    public Withdrawal() {
    }

    public boolean makeWithdrawal(BigDecimal amount, Account account) throws BadRequestException {
        if (account==null || amount == null){return false;}
        if (account.getBalance().getAmount().compareTo(amount) < 0){
            throw new BadRequestException("You have insufficient funds");
        }
        BigDecimal currentAmount = account.getBalance().getAmount();
        BigDecimal newAmount = currentAmount.subtract(amount);
        account.setBalance(new Money(newAmount,EUR));
        account.getTransactionList().add(this);
        return true;
    }
}
