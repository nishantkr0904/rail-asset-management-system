package com.rail.asset.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.rail.asset.config.AuditingConfig;
import com.rail.asset.domain.Asset;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(AuditingConfig.class)
class AssetRepositoryTest {

    @Autowired
    private AssetRepository assetRepository;

    @Test
    void findByAssetCodeReturnsMatch() {
        Asset asset = persistAsset("REP-001", "TRACK", "ACTIVE", "LOC-A");

        assertThat(assetRepository.findByAssetCode("REP-001")).contains(asset);
    }

    @Test
    void findByCategoryAndStatusFiltersResults() {
        persistAsset("REP-100", "POWER", "ACTIVE", "LOC-A");
        persistAsset("REP-101", "POWER", "INACTIVE", "LOC-A");

        List<Asset> results = assetRepository.findByCategoryAndStatus("POWER", "ACTIVE");

        assertThat(results).hasSize(1)
            .allMatch(asset -> asset.getAssetCode().equals("REP-100"));
    }

    @Test
    void findByLocationCodeReturnsMatches() {
        persistAsset("REP-200", "SIGNALING", "ACTIVE", "DEP-1");
        persistAsset("REP-201", "SIGNALING", "ACTIVE", "DEP-2");

        List<Asset> results = assetRepository.findByLocationCode("DEP-1");

        assertThat(results)
            .extracting(Asset::getAssetCode)
            .containsExactly("REP-200");
    }

    private Asset persistAsset(String code, String category, String status, String location) {
        Asset asset = new Asset(
            null,
            code,
            "Component " + code,
            category,
            null,
            "Acme",
            "MDL",
            null,
            LocalDate.of(2020, 1, 1),
            status,
            location,
            30,
            LocalDate.of(2024, 1, 1),
            new BigDecimal("1.00"),
            new BigDecimal("1000.00"),
            null
        );
        return assetRepository.save(asset);
    }
}
