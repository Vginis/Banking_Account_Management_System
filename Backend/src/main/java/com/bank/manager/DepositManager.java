package com.bank.manager;

import com.bank.constant.ErrorMessages;
import com.bank.domain.Account;
import com.bank.domain.Deposit;
import com.bank.domain.Money;
import com.bank.exception.EntityNotFoundException;
import com.bank.mapper.DepositMapper;
import com.bank.repository.AccountRepository;
import com.bank.repository.DepositRepository;
import com.bank.representation.deposit.DepositCreateRepresentation;
import com.bank.representation.deposit.DepositRepresentation;
import com.bank.util.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class DepositManager {
    @Autowired
    public DepositRepository depositRepository;
    @Autowired
    public AccountRepository accountRepository;
    @Autowired
    public DepositMapper depositMapper;

    public List<DepositRepresentation> findAllDeposits(){
        return depositMapper.toRepresentationList(depositRepository.findAll());
    }

    public DepositRepresentation findDepositById(Integer id){
        Optional<Deposit> depositOptional = depositRepository.findById(id);
        if(depositOptional.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Deposit deposit = depositOptional.get();
        return depositMapper.toRepresentation(deposit);
    }

    public List<DepositRepresentation> findDepositsByAccount(Integer accountNumber){
        List<Deposit> deposits = depositRepository.findDepositByAccountNumber(accountNumber);
        return depositMapper.toRepresentationList(deposits);
    }

    public void createDeposit(DepositCreateRepresentation depositCreateRepresentation){
        Optional<Account> accountOptional = accountRepository.findById(depositCreateRepresentation.account);
        if(accountOptional.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Account account = accountOptional.get();
        Deposit deposit = new Deposit();
        deposit.setAccount(account);
        deposit.setAmount(new Money(BigDecimal.valueOf(depositCreateRepresentation.amount), Currency.EUR));
        deposit.setDate(LocalDateTime.now());

        account.depositMoney(depositCreateRepresentation.amount);
        depositRepository.save(deposit);
    }

    public void updateDeposit(Integer transactionId, DepositCreateRepresentation depositCreateRepresentation){
        Optional<Deposit> depositOptional = depositRepository.findById(transactionId);
        Optional<Account> accountOptional = accountRepository.findById(depositCreateRepresentation.account);
        if(depositOptional.isEmpty() || accountOptional.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Deposit deposit = depositOptional.get();
        deposit.updateDepositDetails(depositCreateRepresentation, accountOptional.get());
        depositRepository.save(deposit);
    }

    public void deleteDeposit(Integer transactionId){
        Optional<Deposit> depositOptional = depositRepository.findById(transactionId);
        if(depositOptional.isEmpty()){
            throw new EntityNotFoundException((ErrorMessages.ENTITY_NOT_FOUND));
        }

        depositRepository.delete(depositOptional.get());
    }
}
