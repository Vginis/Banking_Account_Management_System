package com.bank.repository;

import com.bank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.RequestScope;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
}