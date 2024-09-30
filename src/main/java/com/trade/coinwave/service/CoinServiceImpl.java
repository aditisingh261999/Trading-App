package com.trade.coinwave.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trade.coinwave.model.Coin;
import com.trade.coinwave.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CoinServiceImpl implements CoinService {

    private final RestTemplate restTemplate;

    public CoinServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Autowired
    CoinRepository coinRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public List<Coin> getCoins(int page) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page="+page;

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<Coin>>() {});

        } catch (HttpStatusCodeException e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getMarketChart(String coinId, int days) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins"+coinId+"/market_charts?vs_currency=usd&days"+days;

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return responseEntity.getBody();

        } catch (HttpClientErrorException e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getCoinDetails(String coinId) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins"+coinId;

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode node = objectMapper.readTree(responseEntity.getBody());
            Coin coin = new Coin();
            coin.setId(coinId);
            coin.setName(node.get("name").asText());
            coin.setSymbol(node.get("symbol").asText());
            coin.setImage(node.get("image").get("Large").asText());

            JsonNode marketData = node.get("marketData");

            coin.setCurrentPrice(marketData.get("currentPrice").get("usd").asDouble());
            coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
            coin.setMarketCapRank(marketData.get("market_cap").asInt());
            coin.setTotalVolume(marketData.get("totalVolume").get("usd").asLong());
            coin.setHigh24h(marketData.get("high_24h").get("usd").asDouble());
            coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());
            coin.setPriceChange24h(marketData.get("price_change_24h").get("usd").asDouble());
            coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").get("usd").asDouble());
            coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").get("usd").asLong());
            coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").get("usd").asLong());
            coin.setTotalSupply(marketData.get("total_supply").get("usd").asLong());

            coinRepository.save(coin);
            return responseEntity.getBody();

        } catch (HttpClientErrorException e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public Coin findById(String coinId) throws Exception {
        Optional<Coin> coin = coinRepository.findById(coinId);
        if (coin.isEmpty()) {
            throw new Exception("Coin not found");
        }
        return coin.get();
    }

    @Override
    public String searchCoin(String keyword) throws Exception {
        String url = "https://api.coingecko.com/api/v3/search?q=" + keyword;

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return responseEntity.getBody();

        } catch (HttpClientErrorException e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getTop50CoinsByMarketCapRank() throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/markets/vs_currency=usd&per_page=50&page=1";

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return responseEntity.getBody();

        } catch (HttpClientErrorException e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getTradingCoins() throws Exception {
        String url = "https://api.coingecko.com/api/v3/search/trading";

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return responseEntity.getBody();

        } catch (HttpClientErrorException e) {
            throw new Exception(e.getMessage());
        }
    }
}
