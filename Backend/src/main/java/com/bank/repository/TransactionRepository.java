package com.bank.repository;

import com.bank.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
}
