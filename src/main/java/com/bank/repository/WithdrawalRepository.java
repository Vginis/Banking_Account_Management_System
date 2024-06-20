package com.bank.repository;

import com.bank.domain.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
public interface WithdrawalRepository extends JpaRepository<Withdrawal,Integer> {
}
