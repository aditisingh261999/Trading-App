package com.trade.coinwave.service;

import com.trade.coinwave.model.Asset;
import com.trade.coinwave.model.Coin;
import com.trade.coinwave.model.User;
import com.trade.coinwave.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {
        Asset asset = new Asset();
        asset.setUser(user);
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        asset.setBuyPrice(coin.getCurrentPrice());
        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(long assetId) throws Exception {
        Optional<Asset> asset = assetRepository.findById(assetId);
        if (asset.isPresent()) {
            return asset.get();
        }
        throw new Exception("Asset not found");
    }

    @Override
    public Asset getAssetByUserIdAndAssetId(long userId, long assetId) {

        return null;
    }

    @Override
    public List<Asset> getUserAssets(long userId) {
        return assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) throws Exception {
        Asset oldAsset = getAssetById(assetId);
        oldAsset.setQuantity(quantity + oldAsset.getQuantity());

        return assetRepository.save(oldAsset);
    }

    @Override
    public Asset getAssetByUserIdAndCoinId(long userId, long coinId) {
        return assetRepository.findByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public void deleteAsset(long assetId) {
        assetRepository.deleteById(assetId);
    }
}
