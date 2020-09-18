/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.repositary;

import com.vm.qsmart2.model.TblServiceBooked;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Phani
 */
public interface ServiceBookedRepositary extends JpaRepository<TblServiceBooked, Long> {
    
    @Query("SELECT distinct tbl FROM TblServiceBooked tbl join fetch tbl.appointment appt join fetch tbl.service ser where ser.branchId = :branchId and appt.patientId = :patientId and (tbl.startTime BETWEEN :fromDate AND :toDate) order by tbl.serviceBookedId desc")
    public List<TblServiceBooked> findServiceBookedByServiceBranchId(@Param("branchId") long branchId, @Param("patientId") long patientId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT tbl FROM TblServiceBooked tbl join fetch tbl.appointment appt join fetch tbl.service ser "
            + " join fetch TblLocation tblLoc on tblLoc.locationId = appt.locationId where tbl.encounterId = :encounterId"
            + " and tblLoc.locationIdentfier = :locationIdentfier ")
    public TblServiceBooked findServiceBookedByServiceEncounterIdAndLocationCode(@Param("encounterId") String encounterId, 
                                                                  @Param("locationIdentfier") String locationCode);
    
    
    @Query("SELECT tbl FROM TblServiceBooked tbl join fetch tbl.appointment appt join fetch tbl.service ser"
            + " where tbl.encounterId = :encounterId"
            + " and appt.locationId = :locationId ")
    public TblServiceBooked findServiceBookedByServiceEncounterIdAndLocationId(@Param("encounterId") String encounterId, 
                                                                  @Param("locationId") long locationId);

    @Query("SELECT tbl FROM TblServiceBooked tbl join fetch tbl.appointment appt where appt.tranId = :tranId "
            + "and tbl.serviceBookedId != :serviceBookedId "
            + "and tbl.serviceStatus In (:serviceStatus)")
    public List<TblServiceBooked> findServiceBookedByTransId(@Param("tranId") String tranId,
            @Param("serviceBookedId") Long serviceBookedId,
            @Param("serviceStatus") List<String> stats);
    
    @Transactional
    @Modifying
    @Query("UPDATE TblServiceBooked tbl  set tbl.serviceStatus= :serviceStatus WHERE tbl.appointment.tranId = :tranId and tbl.serviceStatus IN (:previousStatus) ")
    public int updateTblServiceBooked(@Param("tranId") String tranId, @Param("previousStatus") List<String> previousStatus, @Param("serviceStatus") String serviceStatus);
    
    
    @Query("SELECT tbl from TblServiceBooked tbl join fetch tbl.appointment appt join fetch tbl.service serv where appt.tranId = :tranId"
            + " and serv.serviceId = :serviceId ")
    public TblServiceBooked getServiceBookedByTranIdNdServiceId(@Param("tranId") String tranId, @Param("serviceId") long serviceId);

}
