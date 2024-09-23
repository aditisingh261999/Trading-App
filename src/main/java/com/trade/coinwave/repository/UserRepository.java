package com.trade.coinwave.repository;

import com.trade.coinwave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {
    User findByEmail(String email);

}
