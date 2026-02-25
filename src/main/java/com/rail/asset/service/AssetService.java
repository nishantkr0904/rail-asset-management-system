package com.rail.asset.service;

import com.rail.asset.domain.Asset;
import java.util.List;

public interface AssetService {

    Asset create(Asset asset);

    Asset update(Long id, Asset asset);

    void delete(Long id);

    Asset findById(Long id);

    Asset findByAssetCode(String assetCode);

    List<Asset> findByCategoryAndStatus(String category, String status);

    List<Asset> findByLocation(String locationCode);

    List<Asset> findAll();
}
