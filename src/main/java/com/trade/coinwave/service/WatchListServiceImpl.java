package com.trade.coinwave.service;

import com.trade.coinwave.model.Coin;
import com.trade.coinwave.model.User;
import com.trade.coinwave.model.WatchList;
import com.trade.coinwave.repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchListServiceImpl implements WatchListService {

    @Autowired
    private WatchListRepository watchListRepository;

    @Override
    public WatchList findUserWatchList(Long userId) throws Exception {
        WatchList watchList = watchListRepository.findByUserId(userId);
        if (watchList == null) {
            throw new Exception("Watchlist not found");
        }
        return null;
    }

    @Override
    public WatchList CreateWatchList(User user) {
        WatchList watchList = new WatchList();
        watchList.setUser(user);
        return watchListRepository.save(watchList);
    }

    @Override
    public WatchList findById(Long id) throws Exception {
        Optional<WatchList> watchList = watchListRepository.findById(id);
        if (watchList.isPresent()) {
            return watchList.get();
        }
        throw new Exception("Watchlist not found");
    }

    @Override
    public Coin addItemToWatchList(Coin coin, User user) throws Exception {
        WatchList watchList = findUserWatchList(user.getId());
        if (watchList.getCoins().contains(coin)) {
            watchList.getCoins().remove(coin);
        } else {
            watchList.getCoins().add(coin);
        }
        watchListRepository.save(watchList);
        return coin;
    }
}
