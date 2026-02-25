package com.rail.asset.service.impl;

import com.rail.asset.domain.Asset;
import com.rail.asset.repository.AssetRepository;
import com.rail.asset.service.AssetService;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public Asset create(Asset asset) {
        validateUniqueCode(asset.getAssetCode(), null);
        normalize(asset);
        return assetRepository.save(asset);
    }

    @Override
    public Asset update(Long id, Asset asset) {
        Asset existing = assetRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Asset not found: " + id));

        validateUniqueCode(asset.getAssetCode(), id);
        applyUpdates(existing, asset);
        normalize(existing);
        return assetRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Asset existing = assetRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Asset not found: " + id));
        assetRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Asset findById(Long id) {
        return assetRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Asset not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Asset findByAssetCode(String assetCode) {
        return assetRepository.findByAssetCode(assetCode)
            .orElseThrow(() -> new RuntimeException("Asset not found by code: " + assetCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asset> findByCategoryAndStatus(String category, String status) {
        return assetRepository.findByCategoryAndStatus(category, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asset> findByLocation(String locationCode) {
        return assetRepository.findByLocationCode(locationCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asset> findAll() {
        return assetRepository.findAll();
    }

    private void validateUniqueCode(String assetCode, Long currentId) {
        assetRepository.findByAssetCode(assetCode)
            .ifPresent(existing -> {
                if (currentId == null || !existing.getId().equals(currentId)) {
                    throw new RuntimeException("Asset code already exists: " + assetCode);
                }
            });
    }

    private void applyUpdates(Asset target, Asset source) {
        target.setAssetCode(source.getAssetCode());
        target.setName(source.getName());
        target.setCategory(source.getCategory());
        target.setSubCategory(source.getSubCategory());
        target.setManufacturer(source.getManufacturer());
        target.setModelNumber(source.getModelNumber());
        target.setSerialNumber(source.getSerialNumber());
        target.setInstallDate(source.getInstallDate());
        target.setStatus(source.getStatus());
        target.setLocationCode(source.getLocationCode());
        target.setMaintenanceCycleDays(source.getMaintenanceCycleDays());
        target.setLastInspectionDate(source.getLastInspectionDate());
        target.setDepreciationRate(source.getDepreciationRate());
        target.setAcquisitionCost(source.getAcquisitionCost());
        target.setNotes(source.getNotes());
    }

    private void normalize(Asset asset) {
        if (asset.getAssetCode() != null) {
            asset.setAssetCode(asset.getAssetCode().trim().toUpperCase(Locale.ROOT));
        }
        if (asset.getLocationCode() != null) {
            asset.setLocationCode(asset.getLocationCode().trim().toUpperCase(Locale.ROOT));
        }
        if (asset.getStatus() != null) {
            asset.setStatus(asset.getStatus().trim().toUpperCase(Locale.ROOT));
        }
    }
}
