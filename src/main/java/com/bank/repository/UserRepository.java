package com.bank.repository;

import com.bank.domain.Card;
import com.bank.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@RequestScope
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT c FROM User c WHERE c.username = :id")
    Optional<User> findByName(@Param("id") String username);
}
