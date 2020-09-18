/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblService;
import com.vm.qsmart2.model.TblUser;
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
public interface ServiceRepository extends JpaRepository<TblService, Long> {

    @Query("SELECT distinct tbl FROM TblService tbl left join fetch tbl.apptTypes where tbl.branchId= :branchId")
    public List<TblService> getAllServicesByBranchId(@Param("branchId") long branchId);

    @Query("SELECT COUNT(tbl) FROM TblService tbl WHERE UPPER(tbl.serviceNameEn)= :serviceNameEn and tbl.branchId= :branchId")
    public int validateServiceEngName(@Param("serviceNameEn") String serviceNameEn, @Param("branchId") Long branchId);

    @Query("SELECT COUNT(tbl) FROM TblService tbl WHERE UPPER(tbl.serviceNameAr)= :serviceNameAr and tbl.branchId= :branchId")
    public int validateServiceNameArb(@Param("serviceNameAr") String serviceNameAr, @Param("branchId") Long branchId);

    @Query("SELECT COUNT(tbl) FROM TblService tbl WHERE UPPER(tbl.serviceNameEn)= :serviceNameEn and tbl.serviceId != :serviceId and tbl.branchId= :branchId")
    public int validateServiceEngNameByServiceId(@Param("serviceNameEn") String serviceNameEn, @Param("serviceId") Long serviceId, @Param("branchId") Long branchId);

    @Query("SELECT COUNT(tbl) FROM TblService tbl WHERE UPPER(tbl.serviceNameAr)= :serviceNameAr and tbl.serviceId != :serviceId and tbl.branchId= :branchId")
    public int validateServiceArbNameByServiceId(@Param("serviceNameAr") String serviceNameAr, @Param("serviceId") Long serviceId, @Param("branchId") Long branchId);

    @Query("SELECT COUNT(tbl) FROM TblService tbl WHERE tbl.branchId= :branchId")
    public int countServicesByBranchId(@Param("branchId") Long branchId);

    @Query("SELECT tbl FROM TblService tbl where tbl.branchId= :branchId")
    public TblService mappingServiceWithApptTypes(@Param("branchId") long branchId);

    @Query("SELECT tbl FROM TblService tbl left join fetch tbl.apptTypes left join fetch tbl.displayBoards dbs where tbl.serviceId= :servicId")
    public TblService findServiceByServiceId(@Param("servicId") long servicId);

    @Query("SELECT tbl FROM TblService tbl where tbl.branchId IN (:branches)")
    public List<TblService> getAllServicesByBranches(@Param("branches") List<Long> branches);

    @Transactional
    @Modifying
    @Query("update TblService tbl set tbl.serviceNameEn= :serviceNameEn, tbl.serviceNameAr= :serviceNameAr, "
            + "tbl.tokenPrefix= :tokenPrefix, tbl.startSeq= :startSeq, tbl.endSeq= :endSeq, tbl.waitTimeAvg= :waitTimeAvg, "
            + "tbl.minCheckinTime= :minCheckinTime, tbl.maxCheckinTime= :maxCheckinTime, tbl.updatedBy= :userId, tbl.updatedOn= :date "
            + "where tbl.serviceId= :serviceId")
    public int updateService(@Param("serviceId") Long serviceId,
            @Param("serviceNameEn") String serviceNameEn,
            @Param("serviceNameAr") String serviceNameAr,
            @Param("tokenPrefix") String tokenPrefix,
            @Param("startSeq") int startSeq,
            @Param("endSeq") int endSeq,
            @Param("waitTimeAvg") int waitTimeAvg,
            @Param("minCheckinTime") int minCheckinTime,
            @Param("maxCheckinTime") int maxCheckinTime,
            @Param("userId") long userId,
            @Param("date") Date date);

//    @Query("SELECT new com.vm.qsmart2api.dtos.user.DoctorDto((u.userId) as drId, concat(u.lastName,' ',u.firstName) as doctorName, at.apptTypeId) FROM TblService service "
//            + " left join TblApptType at on at.apptTypeId = service.apptTypes.apptTypeId "
//            + " left join TblUser u on u.userId = at.users.userId "
//            + " left join TblRole r on r.roleId = u.roles.roleId "
//            + " where r.roleCode= :roleCode and service.serviceId= :serviceId")

    @Query("SELECT ts from TblService ts "
            + "left join fetch ts.apptTypes ats "
            + "left join fetch ats.users u "
            + "left join fetch u.roles r "
            + "where r.roleCode = :roleCode and ts.serviceId = :serviceId")
    public TblService getAllDoctorsByServiceId(@Param("roleCode") String roleCode, @Param("serviceId") long serviceId);
    
    @Query("SELECT ts from TblService ts where ts.branchId= :branchId and ts.isDefault= :isDefault")
    public TblService getDefaultServiceByBranchId(@Param("branchId") Long branchId, @Param("isDefault") short isDefault);
}
