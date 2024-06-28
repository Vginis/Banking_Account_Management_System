package com.bank.repository;

import com.bank.domain.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RequestScope
public interface WithdrawalRepository extends JpaRepository<Withdrawal,Integer> {
    @Query("SELECT c FROM Withdrawal c WHERE c.account.accountNumber = :id")
    List<Withdrawal> findDepositByAccountNumber(@Param("id") Integer id);
}
