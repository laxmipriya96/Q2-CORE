/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblApptType;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Tejasri
 */
public interface ApptTypeRepository extends JpaRepository<TblApptType, Long> {

    @Query("SELECT distinct a FROM TblApptType a left join fetch a.users users left join fetch users.enterprise ent WHERE a.locationId = :locationId")
    public List<TblApptType> findApptTypesWithResourcesByLocationId(@Param("locationId") Long locationId);
    
    @Query("SELECT distinct a FROM TblApptType a WHERE a.locationId = :locationId")
    public List<TblApptType> findAptTypsByLocationId(@Param("locationId") Long locationId);

    @Query("SELECT distinct a FROM TblApptType a left join fetch a.services ser WHERE a.locationId = :locationId and  ser.branchId = :branchId")
    public List<TblApptType> findApptTypesByBranchId(@Param("locationId") Long locationId, @Param("branchId") Long branchId);

    @Query("SELECT a FROM TblApptType a left join fetch a.services left join fetch a.users WHERE a.apptTypeId = :apptTypeId")
    public TblApptType findApptTypeByApptTypeId(@Param("apptTypeId") Long apptTypeId);

    @Query("SELECT COUNT(tbl) FROM TblApptType tbl WHERE tbl.apptType = :apptType and tbl.locationId = :locationId")
    public int validateApptType(@Param("apptType") String apptType, @Param("locationId") Long locationId);

    @Query("SELECT COUNT(tbl) FROM TblApptType tbl WHERE tbl.apptCode = :apptCode and tbl.locationId = :locationId")
    public int validateApptCode(@Param("apptCode") String apptCode, @Param("locationId") Long locationId);

    @Query("SELECT COUNT(tbl) FROM TblApptType tbl WHERE tbl.apptType = :apptType and tbl.apptTypeId != :apptTypeId and tbl.locationId = :locationId")
    public int validateApptTypeByApptId(@Param("apptType") String apptType, @Param("apptTypeId") Long apptTypeId, @Param("locationId") Long locationId);

    @Query("SELECT COUNT(tbl) FROM TblApptType tbl WHERE tbl.apptCode = :apptCode and tbl.apptTypeId != :apptTypeId and tbl.locationId = :locationId")
    public int validateApptCodeByApptId(@Param("apptCode") String apptCode, @Param("apptTypeId") Long apptTypeId, @Param("locationId") Long locationId);

    @Transactional
    @Modifying
    @Query("update TblApptType tat set tat.apptType = :apptType, tat.apptCode = :apptCode"
            + ", tat.updatedOn = :updatedOn , tat.updatedBy = :updatedBy "
            + "where tat.apptTypeId = :apptTypeId ")
    public int updateApptType(@Param("apptType") String apptType,
            @Param("apptCode") String apptCode,
            @Param("updatedOn") Date updatedOn,
            @Param("updatedBy") Long updatedBy,
            @Param("apptTypeId") Long apptTypeId);
}
