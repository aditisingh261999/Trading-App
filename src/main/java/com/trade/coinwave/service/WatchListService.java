package com.trade.coinwave.service;

import com.trade.coinwave.model.Coin;
import com.trade.coinwave.model.User;
import com.trade.coinwave.model.WatchList;

public interface WatchListService {

    WatchList findUserWatchList(Long userId) throws Exception;
    WatchList CreateWatchList(User user);
    WatchList findById(Long id) throws Exception;
    Coin addItemToWatchList(Coin coin, User user) throws Exception;
}
