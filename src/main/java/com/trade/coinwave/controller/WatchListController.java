package com.trade.coinwave.controller;

import com.trade.coinwave.model.Coin;
import com.trade.coinwave.model.User;
import com.trade.coinwave.model.WatchList;
import com.trade.coinwave.service.CoinService;
import com.trade.coinwave.service.UserService;
import com.trade.coinwave.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {

    private final WatchListService watchListService;
    private final UserService userService;

    @Autowired
    private CoinService coinService;

    public WatchListController(WatchListService watchListService, UserService userService) {
        this.watchListService = watchListService;
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<WatchList> getUserWatchList(
            @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);
        WatchList watchList = watchListService.findUserWatchList(user.getId());
        return ResponseEntity.ok(watchList);

    }

    @PostMapping("/create")
    public ResponseEntity<WatchList> createWatchList(
            @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);
        WatchList newWatchList = watchListService.CreateWatchList(user);
        return ResponseEntity.ok(newWatchList);

    }

    @GetMapping("/{watchlistId}")
    public ResponseEntity<WatchList> getWatchListById(
            @PathVariable Long watchlistId) throws Exception {

        WatchList watchList = watchListService.findById(watchlistId);
        return ResponseEntity.ok(watchList);

    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addCoinToWatchList(
            @RequestHeader("Authorization") String token,
            @PathVariable Long coinId) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);
        Coin coin = coinService.findById(String.valueOf(coinId));
        Coin addedCoin = watchListService.addItemToWatchList(coin, user);
        return ResponseEntity.ok(addedCoin);

    }

}
