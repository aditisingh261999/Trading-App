package com.trade.coinwave.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trade.coinwave.model.Coin;
import com.trade.coinwave.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @Autowired
    private ObjectMapper ObjectMapper;

    @GetMapping
    ResponseEntity<List<Coin>> getCoinList (@RequestParam("page") int page) throws Exception {
        List<Coin> coins = coinService.getCoins(page);
        return new ResponseEntity<> (coins, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{coinId}/Chart")
    ResponseEntity<JsonNode> getMarketChart(
            @PathVariable String coinId,
            @RequestParam ("days") int days) throws Exception {

        String response = coinService.getMarketChart(coinId, days);
        JsonNode jsonNode = ObjectMapper.readTree(response);
        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

    @GetMapping
    ResponseEntity<String> getCoinDetails(@PathVariable String coinId) throws Exception {
        String coin = coinService.getCoinDetails(coinId);
        if (coin == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(coin, HttpStatus.ACCEPTED);
    }

    @GetMapping
    ResponseEntity<JsonNode> searchCoin(
            @RequestParam ("q") String keyword) throws Exception {

        String coin = coinService.searchCoin(keyword);
        JsonNode jsonNode = ObjectMapper.readTree(coin);
        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

    @GetMapping
    ResponseEntity<JsonNode> getTop50CoinsByMarketCapRank() throws Exception {
        String response = coinService.getTop50CoinsByMarketCapRank();
        JsonNode jsonNode = ObjectMapper.readTree(response);
        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);

    }

    @GetMapping
    ResponseEntity<JsonNode> getTradingCoins() throws Exception {
        String coin = coinService.getTradingCoins();
        JsonNode jsonNode = ObjectMapper.readTree(coin);
        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

}
