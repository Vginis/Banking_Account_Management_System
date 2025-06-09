package com.bank.manager;

import com.bank.constant.ErrorMessages;
import com.bank.domain.Account;
import com.bank.domain.Money;
import com.bank.domain.User;
import com.bank.exception.EntityNotFoundException;
import com.bank.exception.IllegalArgumentException;
import com.bank.mapper.AccountMapper;
import com.bank.repository.AccountRepository;
import com.bank.repository.CardRepository;
import com.bank.repository.UserRepository;
import com.bank.representation.account.AccountRepresentationRequest;
import com.bank.representation.account.AccountRepresentationResponse;
import com.bank.util.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountManager {
    @Autowired
    public AccountRepository accountRepository;

    @Autowired
    public CardRepository cardRepository;

    @Autowired
    public AccountMapper accountMapper;

    @Autowired
    public UserRepository userRepository;


    public List<AccountRepresentationResponse> listAllAccounts(){
        return accountMapper.toRepresentationList(accountRepository.findAll());
    }

    public AccountRepresentationResponse findAccountById(Integer id){
        Optional<Account> accountOptional = accountRepository.findById(id);
        if(accountOptional.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        return accountMapper.accountToRepresentation(accountOptional.get());
    }

    public void createAccount(AccountRepresentationRequest accountRepresentation){
        if(accountRepository.existsById(accountRepresentation.accountNumber)){
            throw new IllegalArgumentException("account_already_exists");
        }
        if(!userRepository.existsById(accountRepresentation.userId)){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Account account = accountMapper.accountRepresentationToModel(accountRepresentation);
        accountRepository.save(account);
    }

    public void updateAccount(Integer accountNumber, AccountRepresentationRequest accountRepresentationRequest){
        Optional<Account> accountOptional = accountRepository.findById(accountNumber);
        Optional<User> userOptional = userRepository.findById(accountRepresentationRequest.userId);
        if(accountOptional.isEmpty() || userOptional.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Optional<Account> otherAccount = accountRepository.findById(accountRepresentationRequest.accountNumber);
        if(otherAccount.isPresent()){
            throw new IllegalArgumentException(ErrorMessages.INVALID_DATA);
        }

        Account account = accountOptional.get();
        account.setAccountNumber(accountRepresentationRequest.accountNumber);
        account.setBalance(new Money(BigDecimal.valueOf(accountRepresentationRequest.balance), Currency.EUR));
        account.setUser(userOptional.get());
    }

    public void deleteAccount(Integer accountNumber){
        if(!accountRepository.existsById(accountNumber)) {
            throw new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        accountRepository.deleteById(accountNumber);
    }
}
