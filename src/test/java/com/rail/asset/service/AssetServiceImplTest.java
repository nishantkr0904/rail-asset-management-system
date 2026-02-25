package com.rail.asset.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rail.asset.domain.Asset;
import com.rail.asset.repository.AssetRepository;
import com.rail.asset.service.impl.AssetServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepository;

    private AssetServiceImpl assetService;

    @BeforeEach
    void setUp() {
        assetService = new AssetServiceImpl(assetRepository);
    }

    @Test
    void createNormalizesAndSavesAsset() {
        Asset input = sampleAsset();
        input.setAssetCode("ram-100");
        input.setLocationCode("loc-01");
        input.setStatus("active");

        when(assetRepository.findByAssetCode("ram-100")).thenReturn(Optional.empty());
        when(assetRepository.save(any(Asset.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Asset created = assetService.create(input);

        ArgumentCaptor<Asset> captor = ArgumentCaptor.forClass(Asset.class);
        verify(assetRepository).save(captor.capture());

        assertThat(captor.getValue().getAssetCode()).isEqualTo("RAM-100");
        assertThat(captor.getValue().getLocationCode()).isEqualTo("LOC-01");
        assertThat(captor.getValue().getStatus()).isEqualTo("ACTIVE");
        assertThat(created).isEqualTo(captor.getValue());
    }

    @Test
    void createThrowsWhenCodeExists() {
        Asset input = sampleAsset();
        when(assetRepository.findByAssetCode(input.getAssetCode())).thenReturn(Optional.of(sampleAsset(1L)));

        assertThatThrownBy(() -> assetService.create(input))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Asset code already exists");
    }

    @Test
    void updatePersistsChanges() {
        Asset incoming = sampleAsset();
        incoming.setAssetCode("ram-101");
        Asset existing = sampleAsset(2L);

        when(assetRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(assetRepository.findByAssetCode("ram-101")).thenReturn(Optional.of(existing));
        when(assetRepository.save(any(Asset.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Asset updated = assetService.update(2L, incoming);

        verify(assetRepository).save(existing);
        assertThat(updated.getAssetCode()).isEqualTo("RAM-101");
        assertThat(updated.getManufacturer()).isEqualTo(incoming.getManufacturer());
    }

    @Test
    void updateThrowsWhenAssetMissing() {
        Asset incoming = sampleAsset();
        when(assetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assetService.update(99L, incoming))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Asset not found");
    }

    @Test
    void deleteRemovesExistingAsset() {
        Asset existing = sampleAsset(5L);
        when(assetRepository.findById(5L)).thenReturn(Optional.of(existing));

        assetService.delete(5L);

        verify(assetRepository).delete(existing);
    }

    @Test
    void findByCategoryAndStatusDelegates() {
        when(assetRepository.findByCategoryAndStatus("TRACK", "ACTIVE")).thenReturn(Collections.emptyList());

        assertThat(assetService.findByCategoryAndStatus("TRACK", "ACTIVE")).isEmpty();
    }

    private Asset sampleAsset() {
        return sampleAsset(null);
    }

    private Asset sampleAsset(Long id) {
        return new Asset(
            id,
            "RAM-100",
            "Wheel Assembly",
            "TRACK",
            "BOGIE",
            "Acme",
            "MDL-1",
            "SER-1",
            LocalDate.of(2020, 1, 1),
            "ACTIVE",
            "LOC-01",
            90,
            LocalDate.of(2024, 1, 1),
            new BigDecimal("2.50"),
            new BigDecimal("100000.00"),
            "notes"
        );
    }
}
