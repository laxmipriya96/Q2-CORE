/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblMedService;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Ashok
 */
public interface MedServiceRepository extends JpaRepository<TblMedService, Long> {

    @Query("SELECT COUNT(tbl) FROM  TblMedService tbl where tbl.medServiceName= :medServiceName and tbl.locationId=:locationId")
    public int validateMedServiceName(@Param("locationId") Long locationId, @Param("medServiceName") String medServiceName);

    @Query("SELECT COUNT(tbl) FROM  TblMedService tbl where tbl.medServiceCode= :medServiceCode and tbl.locationId=:locationId")
    public int validateMedServiceCode(@Param("locationId") Long locationId, @Param("medServiceCode") String medServiceCode);

    @Query("SELECT COUNT(tbl) FROM  TblMedService tbl where tbl.medServiceCode= :medServiceCode and tbl.locationId= :locationId and (tbl.medServiceId != :medServiceId)")
    public int validateMedServiceCodeByMedServiceId(@Param("medServiceId") Long medServiceId, @Param("locationId") Long locationId, @Param("medServiceCode") String medServiceCode);

    @Query("SELECT COUNT(tbl) FROM  TblMedService tbl where tbl.medServiceName= :medServiceName and tbl.locationId= :locationId and (tbl.medServiceId != :medServiceId)")
    public int validateMedServiceNameByMedServiceId(@Param("medServiceId") Long medServiceId, @Param("locationId") Long locationId, @Param("medServiceName") String medServiceName);

    @Query("SELECT distinct tbl FROM  TblMedService tbl where tbl.locationId= :locationId")
    public List<TblMedService> getAllMedServicesWithUsers(@Param("locationId") Long locationId);

    @Query("SELECT tbl FROM  TblMedService tbl left join fetch tbl.users users where tbl.medServiceId= :medServiceId")
    public TblMedService checkUsersByMedServiceId(@Param("medServiceId") Long medServiceId);

    @Transactional
    @Modifying
    @Query("update TblMedService tms set tms.medServiceName = :medServiceName, tms.medServiceCode = :medServiceCode"
            + ", tms.updatedOn = :updatedOn , tms.updatedBy = :updatedBy "
            + "where tms.medServiceId = :medServiceId ")
    public int updateMedicalService(@Param("medServiceName") String medServiceName,
            @Param("medServiceCode") String medServiceCode,
            @Param("updatedOn") Date updatedOn,
            @Param("updatedBy") Long updatedBy,
            @Param("medServiceId") Long medServiceId);

}
