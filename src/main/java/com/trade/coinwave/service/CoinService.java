package com.trade.coinwave.service;

import com.trade.coinwave.model.Coin;

import java.util.List;

public interface CoinService {
    List<Coin> getCoins(int page) throws Exception;
    String getMarketChart(String coinId, int days) throws Exception;
    String getCoinDetails(String coinId) throws Exception;
    Coin findById(String coinId) throws Exception;
    String searchCoin(String keyword) throws Exception;
    String top50CoinsByMarketCapRank() throws Exception;
    String GetTradingCoins() throws Exception;

}
