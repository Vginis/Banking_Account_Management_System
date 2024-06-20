package com.bank.representation;

import com.bank.domain.Account;
import com.bank.domain.Deposit;
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
public abstract class DepositMapper {
    @Inject
    AccountRepository accountRepository;

    @Mapping(source = "deposit.transactionId", target = "transactionId")
    @Mapping(source = "deposit.account.accountNumber", target = "account")
    @Mapping(source = "deposit.amount.amount", target = "amount")
    @Mapping(target = "date", ignore = true)
    public abstract DepositRepresentation toRepresentation(Deposit deposit);

    public abstract List<DepositRepresentation> toRepresentationList(List<Deposit> depositList);

    @Mapping(source = "transactionId", target = "transactionId")
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "date", ignore = true)
    public abstract Deposit toModel(DepositRepresentation depositRepresentation) throws ParseException;
    @AfterMapping
    public void resolveDateToString(Deposit deposit, @MappingTarget DepositRepresentation depositRepresentation){
        LocalDateTime calendar = deposit.getDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        depositRepresentation.date = calendar.format(formatter);
    }

    @AfterMapping
    public void resolveIntegerToAccount(DepositRepresentation depositRepresentation, @MappingTarget Deposit deposit){
        Account account = null;
        if(depositRepresentation.account != null){
            account = accountRepository.getReferenceById(depositRepresentation.account);
        }
        deposit.setAccount(account);
    }

    @AfterMapping
    public void resolveBDtoBD(DepositRepresentation depositRepresentation, @MappingTarget Deposit deposit) {
        deposit.setAmount(new Money(depositRepresentation.amount, Currency.EUR));
    }

    @AfterMapping
    public void resolveStringToCalendar(DepositRepresentation depositRepresentation, @MappingTarget Deposit deposit){
        String dateString = depositRepresentation.date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        deposit.setDate(dateTime);
    }
}
