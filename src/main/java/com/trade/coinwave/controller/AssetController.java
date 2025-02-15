package com.trade.coinwave.controller;

import com.trade.coinwave.model.Asset;
import com.trade.coinwave.model.User;
import com.trade.coinwave.service.AssetService;
import com.trade.coinwave.service.UserService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/asset")
public class AssetController {

    private final AssetService assetService;

    @Autowired
    private UserService userService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public ResponseEntity<Asset> getAssetById(@PathVariable Long assetId) throws Exception {
        Asset asset = assetService.getAssetById(assetId);
        return ResponseEntity.ok().body(asset);
    }

    @GetMapping
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(
            @PathVariable Long coinId,
            @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);
        Asset asset = assetService.getAssetByUserIdAndCoinId(user.getId(), coinId);
        return ResponseEntity.ok().body(asset);
    }

    @GetMapping
    public ResponseEntity<List<Asset>> getAssetsForUser(
            @RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserProfileByJwtToken(token);
        List<Asset> asset = assetService.getUserAssets(user.getId());
        return ResponseEntity.ok().body(asset);
    }
}
