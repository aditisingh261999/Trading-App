package com.trade.coinwave.service;

import com.trade.coinwave.model.Asset;
import com.trade.coinwave.model.Coin;
import com.trade.coinwave.model.User;
import com.trade.coinwave.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {
        return null;
    }

    @Override
    public Asset getAssetById(long assetId) {
        return null;
    }

    @Override
    public Asset getAssetByUserIdAndAssetId(long userId, long assetId) {
        return null;
    }

    @Override
    public List<Asset> getUserAssets(long userId) {
        return List.of();
    }

    @Override
    public Asset updateAsset(Long assetId) {
        return null;
    }

    @Override
    public Asset getAssetByUserIdAndCoinId(long userId, long coinId) {
        return null;
    }

    @Override
    public void deleteAsset(long assetId) {
        assetRepository.deleteById(assetId);
    }
}
