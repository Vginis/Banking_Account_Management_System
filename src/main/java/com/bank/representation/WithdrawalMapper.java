package com.bank.representation;

import com.bank.domain.Account;
import com.bank.domain.Deposit;
import com.bank.domain.Withdrawal;
import com.bank.repository.AccountRepository;
import com.bank.util.Currency;
import com.bank.util.Money;
import jakarta.inject.Inject;
import org.mapstruct.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {Collectors.class},
        uses = {})
public abstract class WithdrawalMapper {
    @Inject
    AccountRepository accountRepository;

    @Mapping(source = "withdrawal.transactionId", target = "transactionId")
    @Mapping(source = "withdrawal.account.accountNumber", target = "account")
    @Mapping(source = "withdrawal.amount.amount", target = "amount")
    @Mapping(target = "date", ignore = true)
    public abstract WithdrawalRepresentation toRepresentation(Withdrawal withdrawal);

    public abstract List<WithdrawalRepresentation> toRepresentationList(List<Withdrawal> depositList);

    @Mapping(source = "transactionId", target = "transactionId")
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "date", ignore = true)
    public abstract Withdrawal toModel(WithdrawalRepresentation withdrawalRepresentation);

    @AfterMapping
    public void resolveDateToString(Withdrawal withdrawal, @MappingTarget WithdrawalRepresentation withdrawalRepresentation){
        LocalDateTime calendar = withdrawal.getDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        withdrawalRepresentation.date = calendar.format(formatter);
    }

    @AfterMapping
    public void resolveIntegerToAccount(WithdrawalRepresentation withdrawalRepresentation, @MappingTarget Withdrawal withdrawal){
        Account account = null;
        if(withdrawalRepresentation.account != null){
            account = accountRepository.getReferenceById(withdrawalRepresentation.account);
        }
        withdrawal.setAccount(account);
    }

    @AfterMapping
    public void resolveBDtoBD(WithdrawalRepresentation withdrawalRepresentation, @MappingTarget Withdrawal withdrawal) {
        withdrawal.setAmount(new Money(withdrawalRepresentation.amount, Currency.EUR));
    }

    @AfterMapping
    public void resolveStringToCalendar(WithdrawalRepresentation withdrawalRepresentation, @MappingTarget Withdrawal withdrawal){
        String dateString = withdrawalRepresentation.date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        withdrawal.setDate(dateTime);
    }
}
