package com.bank.repository;

import com.bank.domain.Card;
import com.bank.domain.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit,Integer> {
    @Query("SELECT c FROM Deposit c WHERE c.account.accountNumber = :id")
    List<Deposit> findDepositByAccountNumber(@Param("id") Integer id);
}
