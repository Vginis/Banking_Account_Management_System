package com.bank.repository;

import com.bank.domain.Account;
import com.bank.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RequestScope
public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT c FROM Card c WHERE c.cardId = :id")
    Card findCardById(@Param("id") Long id);

    @Query("SELECT c FROM Card c WHERE c.account = :account")
    List<Card> findCardByAccount(@Param("account") Account account);

}
