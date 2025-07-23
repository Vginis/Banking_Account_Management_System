package com.bank.manager;

import com.bank.constant.ErrorMessages;
import com.bank.domain.Account;
import com.bank.domain.Money;
import com.bank.domain.Withdrawal;
import com.bank.exception.EntityNotFoundException;
import com.bank.exception.IllegalArgumentException;
import com.bank.mapper.WithdrawalMapper;
import com.bank.repository.AccountRepository;
import com.bank.repository.WithdrawalRepository;
import com.bank.representation.withdrawal.WithdrawalCreateRepresentation;
import com.bank.representation.withdrawal.WithdrawalRepresentation;
import com.bank.util.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalManager {
    @Autowired
    public WithdrawalRepository withdrawalRepository;
    @Autowired
    public AccountRepository accountRepository;
    @Autowired
    public WithdrawalMapper withdrawalMapper;

    public List<WithdrawalRepresentation> listAllWithdrawals(){
        return withdrawalMapper.toRepresentationList(withdrawalRepository.findAll());
    }

    public WithdrawalRepresentation getWithdrawalById(Integer id){
        Optional<Withdrawal> withdrawalOptional = withdrawalRepository.findById(id);
        if(withdrawalOptional.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        return withdrawalMapper.toRepresentation(withdrawalOptional.get());
    }

    public List<WithdrawalRepresentation> listWithdrawalsByAccountNumber(Integer accountNumber){
        List<Withdrawal> withdrawals = withdrawalRepository.findDepositByAccountNumber(accountNumber);
        return withdrawalMapper.toRepresentationList(withdrawals);
    }

    public void createWithdrawal(WithdrawalCreateRepresentation withdrawalCreateRepresentation) throws IllegalArgumentException{
        Optional<Account> accountOptional = accountRepository.findById(withdrawalCreateRepresentation.account);
        if(accountOptional.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }
        Account account = accountOptional.get();
        if(account.getBalance().getAmount().compareTo(BigDecimal.valueOf(withdrawalCreateRepresentation.amount)) < 0){
            throw new IllegalArgumentException(ErrorMessages.INSUFFICIENT_FUNDS);
        }

        Withdrawal withdrawal = new Withdrawal(null, LocalDateTime.now(), new Money(BigDecimal.valueOf(withdrawalCreateRepresentation.amount), Currency.EUR),
                accountOptional.get());
        account.withDrawMoney(withdrawalCreateRepresentation.amount);
        withdrawalRepository.save(withdrawal);
    }

    public void deleteWithdrawal(Integer transactionId){
        Optional<Withdrawal> withdrawalOptional = withdrawalRepository.findById(transactionId);
        if(withdrawalOptional.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }
        withdrawalRepository.delete(withdrawalOptional.get());
    }





}
