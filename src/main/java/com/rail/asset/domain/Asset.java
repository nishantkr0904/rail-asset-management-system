package com.rail.asset.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "assets", indexes = {
        @Index(name = "idx_asset_category_status", columnList = "category,status"),
        @Index(name = "idx_asset_location_code", columnList = "location_code"),
        @Index(name = "idx_asset_last_inspection_date", columnList = "last_inspection_date")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_asset_code", columnNames = "asset_code")
})
@EntityListeners(AuditingEntityListener.class)
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_code", nullable = false, length = 64)
    private String assetCode;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "category", nullable = false, length = 64)
    private String category;

    @Column(name = "sub_category", length = 64)
    private String subCategory;

    @Column(name = "manufacturer", length = 64)
    private String manufacturer;

    @Column(name = "model_number", length = 64)
    private String modelNumber;

    @Column(name = "serial_number", length = 64)
    private String serialNumber;

    @Column(name = "install_date")
    private LocalDate installDate;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "location_code", length = 64)
    private String locationCode;

    @Column(name = "maintenance_cycle_days")
    private Integer maintenanceCycleDays;

    @Column(name = "last_inspection_date")
    private LocalDate lastInspectionDate;

    @Column(name = "depreciation_rate", precision = 5, scale = 2)
    private BigDecimal depreciationRate;

    @Column(name = "acquisition_cost", precision = 15, scale = 2)
    private BigDecimal acquisitionCost;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false, length = 64)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 64)
    private String lastModifiedBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Asset() {
        // JPA constructor
    }

    public Asset(
            Long id,
            String assetCode,
            String name,
            String category,
            String subCategory,
            String manufacturer,
            String modelNumber,
            String serialNumber,
            LocalDate installDate,
            String status,
            String locationCode,
            Integer maintenanceCycleDays,
            LocalDate lastInspectionDate,
            BigDecimal depreciationRate,
            BigDecimal acquisitionCost,
            String notes) {
        this.id = id;
        this.assetCode = assetCode;
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.manufacturer = manufacturer;
        this.modelNumber = modelNumber;
        this.serialNumber = serialNumber;
        this.installDate = installDate;
        this.status = status;
        this.locationCode = locationCode;
        this.maintenanceCycleDays = maintenanceCycleDays;
        this.lastInspectionDate = lastInspectionDate;
        this.depreciationRate = depreciationRate;
        this.acquisitionCost = acquisitionCost;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getInstallDate() {
        return installDate;
    }

    public void setInstallDate(LocalDate installDate) {
        this.installDate = installDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public Integer getMaintenanceCycleDays() {
        return maintenanceCycleDays;
    }

    public void setMaintenanceCycleDays(Integer maintenanceCycleDays) {
        this.maintenanceCycleDays = maintenanceCycleDays;
    }

    public LocalDate getLastInspectionDate() {
        return lastInspectionDate;
    }

    public void setLastInspectionDate(LocalDate lastInspectionDate) {
        this.lastInspectionDate = lastInspectionDate;
    }

    public BigDecimal getDepreciationRate() {
        return depreciationRate;
    }

    public void setDepreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public BigDecimal getAcquisitionCost() {
        return acquisitionCost;
    }

    public void setAcquisitionCost(BigDecimal acquisitionCost) {
        this.acquisitionCost = acquisitionCost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
