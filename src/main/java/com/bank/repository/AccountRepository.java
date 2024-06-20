package com.bank.repository;

import com.bank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.web.context.annotation.RequestScope;



@RequestScope
public interface AccountRepository extends JpaRepository<Account,Integer> {
}