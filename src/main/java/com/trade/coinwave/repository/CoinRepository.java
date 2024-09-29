package com.trade.coinwave.repository;

import com.trade.coinwave.model.Coin;
import com.trade.coinwave.service.CoinService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, String> {
}
