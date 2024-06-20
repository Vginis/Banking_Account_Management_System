package com.bank.repository;

import com.bank.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
public interface UserRepository extends JpaRepository<User,Integer> {
}
