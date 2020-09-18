/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblLocation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Tejasri
 */
public interface LocationRepository extends JpaRepository<TblLocation, Long>{
    
    @Query("SELECT COUNT(tbl) FROM TblLocation tbl WHERE UPPER(tbl.locationNameEn) = :locationNameEn and tbl.tblenterprise.enterpriseId = :enterpriseId")
    public int validateLocationEngName(@Param("locationNameEn") String branchEngName, @Param("enterpriseId") Long enterpriseId);

    @Query("SELECT COUNT(tbl) FROM TblLocation tbl WHERE UPPER(tbl.locationNameAr) = :locationNameAr and tbl.tblenterprise.enterpriseId = :enterpriseId")
    public int validateLocationNameArbInDb(@Param("locationNameAr") String locationNameAr, @Param("enterpriseId") Long enterpriseId);
    
    @Query("SELECT COUNT(tbl) FROM TblLocation tbl WHERE UPPER(tbl.locationNameEn)= :locationNameEn and tbl.locationId != :locationId and tbl.tblenterprise.enterpriseId = :enterpriseId")
    public int validateLocationEngNameByLocationId(@Param("locationNameEn") String locationNameEn, @Param("locationId") Long locationId, @Param("enterpriseId") Long enterpriseId);

    @Query("SELECT COUNT(tbl) FROM TblLocation tbl WHERE UPPER(tbl.locationNameAr) = :locationNameAr and tbl.locationId != :locationId and tbl.tblenterprise.enterpriseId = :enterpriseId")
    public int validateLoactionArbNameByLocationId(@Param("locationNameAr") String locationNameAr, @Param("locationId") Long locationId, @Param("enterpriseId") Long enterpriseId);

    @Query("SELECT distinct tbl FROM TblLocation tbl join fetch tbl.tblenterprise enter where enter.enterpriseId = :enterpriseId order by tbl.locationId DESC")
    public List<TblLocation> findByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    
    @Query("SELECT distinct tbl,b,s FROM TblLocation tbl join fetch tbl.tblenterprise enter "
            + "left join TblBranch b on tbl.locationId = b.location.locationId "
            + "left join TblService s on b.branchId = s.branchId "
            + "where enter.enterpriseId = :enterpriseId")
    public List<Object[]> getAllLocationsBranchesAndServsByEntId(@Param("enterpriseId") Long enterpriseId);
    
    @Query("SELECT tbl.locationId FROM TblLocation tbl WHERE UPPER(tbl.locationNameEn) = :locationNameEn and tbl.tblenterprise.enterpriseId = :enterpriseId")
    public Long getLocationIdByLocationName(@Param("locationNameEn") String locationNameEn, @Param("enterpriseId") Long enterpriseId);

}   

