/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblServiceLocation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Phani
 */
public interface ServiceLocationRepositary extends JpaRepository<TblServiceLocation, Long>{
    
    @Query("SELECT COUNT(tbl) FROM TblServiceLocation tbl WHERE tbl.serviceLocation = :serviceLocation and tbl.locationId= :locationId")
    public int validateServiceLocationInDb(@Param("serviceLocation") String serviceLocation, @Param("locationId") Long locationId);

    @Query("SELECT distinct tbl FROM TblServiceLocation tbl left join fetch tbl.branches brnchs WHERE tbl.serviceLocation = :serviceLocation and brnchs.branchId != :branchId and tbl.locationId= :locationId")
    public List<TblServiceLocation> validateServiceLocationByBranchId(@Param("serviceLocation") String serviceLocation, @Param("branchId") Long branchId, @Param("locationId") Long locationId);

    
    @Query("SELECT tbl FROM TblServiceLocation tbl WHERE tbl.serviceLocation = :serviceLocation and tbl.locationId= :locationId")
    public TblServiceLocation getServiceLocationByServiceLocationNdLocationId(@Param("serviceLocation") String serviceLocation, @Param("locationId") Long locationId);
}
