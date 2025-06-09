package com.bank.mapper;

import com.bank.domain.Account;
import com.bank.domain.Money;
import com.bank.domain.User;
import com.bank.repository.CardRepository;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import com.bank.representation.account.AccountRepresentationRequest;
import com.bank.representation.account.AccountRepresentationResponse;
import com.bank.util.Currency;
import jakarta.inject.Inject;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {Collectors.class},
        uses = {CardMapper.class, DepositMapper.class, WithdrawalMapper.class})
public abstract class AccountMapper {
    @Inject
    UserRepository userRepository;
    @Inject
    CardRepository cardRepository;
    @Inject
    TransactionRepository transactionRepository;

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "balance.amount", target = "balance")
    @Mapping(target = "cardList", expression = "java(account.getCardList().stream().map(com.bank.domain.Card::getCardId).collect(Collectors.toList()))")
    @Mapping(target = "transactionList", expression = "java(account.getTransactionList().stream().map(com.bank.domain.Transaction::getTransactionId).collect(Collectors.toList()))")
    public abstract AccountRepresentationResponse accountToRepresentation(Account account);

    public abstract List<AccountRepresentationResponse> toRepresentationList(List<Account> accountList);

    @Mapping(source = "accountNumber",target = "accountNumber")
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "cardList", ignore = true)
    @Mapping(target = "transactionList", ignore = true)
    public abstract Account accountRepresentationToModel(AccountRepresentationRequest accountRepresentation);

    @AfterMapping
    public void resolveUserById(AccountRepresentationRequest accountRepresentation, @MappingTarget Account account){
        User user = null;
        if(accountRepresentation.userId != null){
            user = userRepository.getReferenceById(accountRepresentation.userId);
        }
        account.setUser(user);
    }

    @AfterMapping
    public void resolveBalance(AccountRepresentationRequest accountRepresentation, @MappingTarget Account account){
        Money money = null;
        if(accountRepresentation.balance != null){
            money = new Money(new BigDecimal(accountRepresentation.balance), Currency.EUR);
        }
        account.setBalance(money);
    }
}
