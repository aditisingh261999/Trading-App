package com.trade.coinwave.service;

import com.trade.coinwave.model.Asset;
import com.trade.coinwave.model.Coin;
import com.trade.coinwave.model.User;

import java.util.List;

public interface AssetService {
    Asset createAsset(User user, Coin coin, double quantity);
    Asset getAssetById(long assetId) throws Exception;
    Asset getAssetByUserIdAndAssetId(long userId, long assetId);
    List<Asset> getUserAssets (long userId);
    Asset updateAsset(Long assetId, double quantity) throws Exception;
    Asset getAssetByUserIdAndCoinId(long userId, long coinId);
    void deleteAsset(long assetId);


}
