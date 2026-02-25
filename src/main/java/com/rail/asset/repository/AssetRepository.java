package com.rail.asset.repository;

import com.rail.asset.domain.Asset;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    Optional<Asset> findByAssetCode(String assetCode);

    List<Asset> findByCategoryAndStatus(String category, String status);

    List<Asset> findByLocationCode(String locationCode);
}
