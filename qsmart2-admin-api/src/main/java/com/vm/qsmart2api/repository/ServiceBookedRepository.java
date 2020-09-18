/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblServiceBooked;
import com.vm.qsmart2.utils.TriggerParams;
import com.vm.qsmart2api.dtos.token.TokenDTO;
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
public interface ServiceBookedRepository extends JpaRepository<TblServiceBooked, Long> {

//    @Query(" SELECT distinct new com.vm.qsmart2api.dtos.token.TokenDTO(token.tokenId, token.tokenNo, patient.mrnNo, "
//            + " concat(patient.lastName,' ',patient.firstName) as patientName, "
//            + " concat(user.lastName,' ',user.firstName) as doctorName, "
//            + " appt.startTime, token.createdOn, user.userId, token.priority, token.appaitment.tranId) "
//            + " FROM TblServiceBooked servBooked "
//            + " left join servBooked.appointment appt "
//            + " left join TblToken token ON token.appaitment = appt.tranId "
//            + " left join TblPatient patient ON patient.patientId = appt.patientId "
//            + " left join TblUser user ON user.userId = servBooked.drId "
//            + " where servBooked.service.branchId = :branchId and token.status= :status and (token.createdOn BETWEEN :fromDate AND :toDate) "
//            + " group by token.tokenId , token.tokenNo, patient.mrnNo, patient.lastName, patient.firstName, user.lastName, user.firstName, "
//            + " appt.startTime, token.createdOn, user.userId, token.priority, token.appaitment.tranId")
    @Query(" SELECT distinct new com.vm.qsmart2api.dtos.token.TokenDTO(token.tokenId, token.tokenNo, patient.mrnNo, "
            + " concat(patient.lastName,' ',patient.firstName) as patientName, "
            + " appt.startTime, token.createdOn, token.priority, token.appaitment.tranId, token.updatedOn) "
            + " FROM TblServiceBooked servBooked "
            + " left join servBooked.appointment appt "
            + " left join TblToken token ON token.appaitment = appt.tranId "
            + " left join TblPatient patient ON patient.patientId = appt.patientId "
            + " left join TblUser user ON user.userId = servBooked.drId "
            + " where servBooked.service.branchId = :branchId and token.status= :status and (token.createdOn BETWEEN :fromDate AND :toDate) "
            + " group by token.tokenId, token.tokenNo, patient.mrnNo, patient.lastName, patient.firstName, "
            + " appt.startTime, token.createdOn, token.priority, token.appaitment.tranId, token.updatedOn")
    public List<TokenDTO> getRegistratinWaitingTokensByBranchId(@Param("branchId") long branchId,
            @Param("status") String status,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate);

//    @Query(" SELECT distinct new com.vm.qsmart2api.dtos.token.TokenDTO(token.tokenId, token.tokenNo, patient.mrnNo, "
//            + " concat(patient.lastName,' ',patient.firstName) as patientName, "
//            + " concat(user.lastName,' ',user.firstName) as doctorName, "
//            + " appt.startTime, token.createdOn, user.userId, token.updatedOn,  "
//            + " room.roomId, room.roomNo, token.priority, token.appaitment.tranId) "
//            + " FROM TblServiceBooked servBooked "
//            + " left join servBooked.appointment appt "
//            + " left join TblToken token ON token.appaitment = appt.tranId "
//            + " left join TblPatient patient ON patient.patientId= appt.patientId "
//            + " left join TblUser user ON user.userId= servBooked.drId "
//            + " left join TblRoom room ON room.roomId= token.roomNo "
//            + " where servBooked.service.branchId = :branchId and token.status= :status and (token.createdOn BETWEEN :fromDate AND :toDate) and token.servedBy = :servedBy "
//            + " group by token.tokenId, token.tokenNo, patient.mrnNo, patient.lastName, patient.firstName, user.lastName, user.firstName, "
//            + " appt.startTime, token.createdOn, token.updatedOn, user.userId, room.roomId, room.roomNo, token.priority, token.appaitment.tranId")
//    public List<TokenDTO> getRegistrationServingTokensByBranchIdNdServedBy(@Param("branchId") long branchId,
//            @Param("status") String status,
//            @Param("servedBy") long servedBy,
//            @Param("fromDate") Date fromDate,
//            @Param("toDate") Date toDate);
    @Query(" SELECT distinct new com.vm.qsmart2api.dtos.token.TokenDTO(token.tokenId, token.tokenNo, patient.mrnNo, "
            + " concat(patient.lastName,' ',patient.firstName) as patientName, "
            + " concat(user.lastName,' ',user.firstName) as doctorName, "
            + " appt.startTime, token.createdOn, user.userId, token.updatedOn,  "
            + " room.roomId, room.roomNo, token.priority, token.appaitment.tranId, token.updatedOn) "
            + " FROM TblServiceBooked servBooked "
            + " left join servBooked.appointment appt "
            + " left join TblToken token ON token.appaitment = appt.tranId "
            + " left join TblPatient patient ON patient.patientId= appt.patientId "
            + " left join TblUser user ON user.userId= servBooked.drId "
            + " left join TblRoom room ON room.roomId= token.roomNo "
            + " where servBooked.service.branchId = :branchId and servBooked.serviceStatus = :serviceStatus and token.status= :status and (token.createdOn BETWEEN :fromDate AND :toDate) and token.servedBy = :servedBy "
            + " group by token.tokenId, token.tokenNo, patient.mrnNo, patient.lastName, patient.firstName, user.lastName, user.firstName, "
            + " appt.startTime, token.createdOn, token.updatedOn, user.userId, room.roomId, room.roomNo, token.priority, token.appaitment.tranId")
    public List<TokenDTO> getRegistrationServingTokensByBranchIdNdServedBy(@Param("branchId") long branchId,
            @Param("status") String status,
            @Param("serviceStatus") String serviceStatus,
            @Param("servedBy") long servedBy,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate);

    @Query("SELECT new com.vm.qsmart2api.dtos.token.TokenDTO(token.tokenId, token.tokenNo, patient.mrnNo, concat(patient.lastName,' ',patient.firstName) as patientName, concat(user.lastName,' ',user.firstName) as doctorName, servBooked.startTime, token.createdOn, user.userId, token.priority, token.appaitment.tranId, token.updatedOn) FROM TblServiceBooked servBooked "
            + " left join servBooked.appointment appt "
            + " left join TblToken token ON token.appaitment = appt.tranId "
            + " left join TblPatient patient ON patient.patientId = appt.patientId "
            + " left join TblUser user ON user.userId = servBooked.drId "
            + " where servBooked.service.serviceId = :serviceId and servBooked.serviceStatus IN (:serviceStatus) and token.status= :status and (token.createdOn BETWEEN :fromDate AND :toDate)")
    public List<TokenDTO> getAllWaitingTokensByServiceId(@Param("serviceId") long serviceId, @Param("status") String status, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("serviceStatus") List<String> serviceStatus);

    @Query("SELECT new com.vm.qsmart2api.dtos.token.TokenDTO(token.tokenId, token.tokenNo, patient.mrnNo, concat(patient.lastName,' ',patient.firstName) as patientName, concat(user.lastName,' ',user.firstName) as doctorName, servBooked.startTime, token.createdOn, user.userId, token.updatedOn,  room.roomId, room.roomNo, token.priority, token.appaitment.tranId, token.updatedOn) FROM TblServiceBooked servBooked "
            + " left join servBooked.appointment appt "
            + " left join TblToken token ON token.appaitment = appt.tranId "
            + " left join TblPatient patient ON patient.patientId= appt.patientId "
            + " left join TblUser user ON user.userId= servBooked.drId "
            + " left join TblRoom room ON room.roomId= token.roomNo "
            + " where servBooked.service.serviceId= :serviceId and servBooked.serviceStatus = :serviceStatus and token.status= :status and (token.createdOn BETWEEN :fromDate AND :toDate) and token.servedBy = :servedBy")
    public List<TokenDTO> getAllServingTokensByServiceIdNdServedBy(@Param("serviceId") long serviceId, @Param("status") String status,
            @Param("servedBy") long servedBy, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("serviceStatus") String serviceStatus);

//    @Query("SELECT distinct tbl from TblServiceBooked tbl join fetch tbl.appointment appt join fetch tbl.service serv where appt.tranId = :tranId"
//            + " and serv.serviceId != :serviceId and tbl.status = :status")
//    public List<TblServiceBooked> getServiceBookedByTranId(@Param("tranId") String tranId, @Param("serviceId") long serviceId, @Param("status") int status);
//    
    @Query("SELECT tbl FROM TblServiceBooked tbl "
            + "join fetch tbl.appointment appt "
            + "join fetch tbl.service service "
            + "where appt.tranId = :tranId "
            + "and service.serviceId != :serviceId "
            + "and tbl.serviceStatus In (:serviceStatus)")
    public List<TblServiceBooked> findServiceBookedByTransId(@Param("tranId") String tranId,
            @Param("serviceId") Long serviceId,
            @Param("serviceStatus") List<String> serviceStatus);

    @Query("SELECT tbl FROM TblServiceBooked tbl join fetch tbl.appointment appt where appt.tranId = :tranId "
            + "and tbl.serviceStatus In (:serviceStatus)")
    public List<TblServiceBooked> findRegisterServiceBookedByTransId(@Param("tranId") String tranId,
            @Param("serviceStatus") List<String> serviceStatus);

    @Query("SELECT tbl from TblServiceBooked tbl join fetch tbl.appointment appt join fetch tbl.service serv where appt.tranId = :tranId"
            + " and serv.serviceId = :serviceId ")
    public TblServiceBooked getServiceBookedByTranIdNdServiceId(@Param("tranId") String tranId, @Param("serviceId") long serviceId);

    @Query("SELECT tbl from TblServiceBooked tbl join fetch tbl.appointment appt join fetch tbl.service serv where tbl.serviceBookedId = :serviceBookedId")
    public TblServiceBooked findTblServiceBookedByServiceBookedId(@Param("serviceBookedId") long serviceBookedId);

    @Query("SELECT tbl from TblServiceBooked tbl join fetch tbl.appointment appt join fetch tbl.service serv where appt.tranId = :tranId"
            + " order by tbl.startTime desc ")
    public List<TblServiceBooked> findFirstBytranIdOrderBystartTimeAsc(@Param("tranId") String tranId);

    @Transactional
    @Modifying
    @Query("UPDATE TblServiceBooked tbl  set tbl.serviceStatus= :serviceStatus WHERE tbl.service.serviceId = :serviceId "
            + "and tbl.appointment.tranId = :tranId")
    public int updateServiceBooked(@Param("serviceStatus") String serviceStatus,
            @Param("serviceId") long serviceId,
            @Param("tranId") String tranId);

//    @Transactional
//    @Modifying
//    @Query("UPDATE TblServiceBooked tbl set tbl.serviceStatus= :serviceStatus WHERE "
//            + " tbl.appointment.tranId = :tranId")
//    public int updateServiceBookedByTranId(@Param("serviceStatus") String serviceStatus,
//            @Param("tranId") String tranId);
    @Transactional
    @Modifying
    @Query("UPDATE TblServiceBooked tbl  set tbl.serviceStatus= :serviceStatus WHERE tbl.appointment.tranId = :tranId and tbl.serviceStatus IN (:previousStatus) ")
    public int updateServiceBookedByTranId(@Param("serviceStatus") String serviceStatus, @Param("tranId") String tranId, @Param("previousStatus") List<String> previousStatus);

    @Query("SELECT distinct tbl FROM TblServiceBooked tbl join fetch tbl.appointment appt join fetch tbl.service ser where ser.branchId = :branchId and appt.patientId = :patientId and (tbl.startTime BETWEEN :fromDate AND :toDate) order by tbl.serviceBookedId desc")
    public List<TblServiceBooked> findServiceBookedByServiceBranchId(@Param("branchId") long branchId, @Param("patientId") long patientId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT count(tbl) FROM TblServiceBooked tbl join tbl.service ser where ser.serviceId = :serviceId and (tbl.startTime BETWEEN :fromDate AND :toDate)")
    public int getTotalAppointmentCount(@Param("serviceId") long serviceId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT count(tbl) FROM TblServiceBooked tbl join tbl.service ser where ser.serviceId = :serviceId and tbl.serviceStatus != :serviceStatus and (tbl.startTime BETWEEN :fromDate AND :toDate)")
    public int getTotalCheckedInCount(@Param("serviceId") long serviceId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("serviceStatus") String status);

    @Query("SELECT count(token) FROM TblServiceBooked tbl "
            + "left join tbl.appointment appt "
            + "left join TblToken token ON token.appaitment.tranId = appt.tranId "
            + "where tbl.service.serviceId= :serviceId and token.status in(:tokenStatus) and (token.createdOn BETWEEN :fromDate AND :toDate)")
    public int getTotalWaitingCount(@Param("serviceId") long serviceId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("tokenStatus") List<String> tokenStatus);

    @Query("SELECT AVG(visitsum.totalWaitTime), AVG(visitsum.totalCareTime) FROM TblServiceBooked tbl "
            + "left join fetch TblVisitSummary visitsum on visitsum.transId= tbl.appointment.tranId "
            + "join tbl.service ser where ser.serviceId = :serviceId and (tbl.startTime BETWEEN :fromDate AND :toDate)")
    public List<double[]> getAverageWaitAndCoreTime(@Param("serviceId") long serviceId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT count(tbl) FROM TblServiceBooked tbl join tbl.service ser where ser.branchId = :branchId and (tbl.startTime BETWEEN :fromDate AND :toDate)")
    public int getTotalAppointmentCountByBranchId(@Param("branchId") long branchId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT count(tbl) FROM TblServiceBooked tbl join tbl.service ser where ser.branchId = :branchId and tbl.serviceStatus != :serviceStatus and (tbl.startTime BETWEEN :fromDate AND :toDate)")
    public int getTotalCheckedInCountByBranchId(@Param("branchId") long branchId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("serviceStatus") String serviceStatus);

    @Query("SELECT count(token) FROM TblServiceBooked tbl "
            + "left join tbl.appointment appt "
            + "left join TblToken token ON token.appaitment.tranId = appt.tranId "
            + "where tbl.service.branchId= :branchId and token.status in(:tokenStatus) and (token.createdOn BETWEEN :fromDate AND :toDate)")
    public int getTotalWaitingCountByBranchId(@Param("branchId") long branchId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("tokenStatus") List<String> tokenStatus);

    @Query("SELECT AVG(visitsum.totalWaitTime), AVG(visitsum.totalCareTime) FROM TblServiceBooked tbl "
            + "left join fetch TblVisitSummary visitsum on visitsum.transId= tbl.appointment.tranId "
            + "join tbl.service ser where ser.branchId = :branchId and (tbl.startTime BETWEEN :fromDate AND :toDate)")
    public List<double[]> getAverageWaitAndCoreTimeByBranchId(@Param("branchId") long branchId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    //
    @Query("SELECT new com.vm.qsmart2.utils.TriggerParams(patient.mrnNo, concat(patient.lastName,' ',patient.firstName), patient.mobileNo, patient.mailAddress, loc.locationNameEn, branch.branchNameEn, serv.serviceNameEn, tbl.startTime, concat(user.lastName,' ',user.firstName), enterprise.enterpriseId) FROM TblServiceBooked tbl "
            + "left join tbl.appointment appt "
            + "left join TblPatient patient on patient.patientId=  appt.patientId "
            + "left join tbl.service serv "
            + "left join TblBranch branch on branch.branchId= serv.branchId "
            + "left join branch.location loc "
            + "left join loc.tblenterprise enterprise "
            + "left join TblUser user on user.userId= tbl.drId "
            + "where tbl.appointment.tranId= :tranId and tbl.service.serviceId= :serviceId")
    public TriggerParams getAppointmentDetails(@Param("tranId") String tranId, @Param("serviceId") long serviceId);

    @Query("SELECT new com.vm.qsmart2.utils.TriggerParams(patient.mrnNo, concat(patient.lastName,' ',patient.firstName), patient.mobileNo, patient.mailAddress, loc.locationNameEn, branch.branchNameEn, serv.serviceNameEn, tbl.startTime, concat(user.lastName,' ',user.firstName), token.createdOn, token.tokenNo, enterprise.enterpriseId) FROM TblServiceBooked tbl "
            + "left join tbl.appointment appt "
            + "left join TblToken token on token.appaitment.tranId= appt.tranId "
            + "left join TblPatient patient on patient.patientId=  appt.patientId "
            + "left join tbl.service serv "
            + "left join TblBranch branch on branch.branchId= serv.branchId "
            + "left join branch.location loc "
            + "left join loc.tblenterprise enterprise "
            + "left join TblUser user on user.userId= tbl.drId "
            + "where tbl.appointment.tranId= :tranId and tbl.service.serviceId= :serviceId")
    public TriggerParams getCheckInAppointmentDetails(@Param("tranId") String tranId, @Param("serviceId") long serviceId);

    @Query("SELECT new com.vm.qsmart2.utils.TriggerParams(patient.mrnNo, concat(patient.lastName,' ',patient.firstName), patient.mobileNo, patient.mailAddress, loc.locationNameEn, branch.branchNameEn, serv.serviceNameEn, tbl.startTime, concat(user.lastName,' ',user.firstName), enterprise.enterpriseId) FROM TblServiceBooked tbl "
            + "left join tbl.appointment appt "
            + "left join TblPatient patient on patient.patientId=  appt.patientId "
            + "left join tbl.service serv "
            + "left join TblBranch branch on branch.branchId= serv.branchId "
            + "left join branch.location loc "
            + "left join loc.tblenterprise enterprise "
            + "left join TblUser user on user.userId= tbl.drId "
            + "where tbl.serviceStatus =:serviceStatus and (tbl.startTime BETWEEN :fromDate AND :toDate)")
    public List<TriggerParams> getScheduledAppointments(@Param("serviceStatus") String serviceStatus, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT new com.vm.qsmart2api.dtos.token.TokenDTO(token.tokenId, token.tokenNo, patient.mrnNo, concat(patient.lastName,' ',patient.firstName) as patientName, servBooked.startTime, token.createdOn, token.priority, token.appaitment.tranId, token.updatedOn) FROM TblServiceBooked servBooked "
            + " left join servBooked.appointment appt "
            + " left join TblToken token ON token.appaitment = appt.tranId "
            + " left join TblPatient patient ON patient.patientId = appt.patientId "
            + " where servBooked.service.serviceId = :serviceId and servBooked.serviceStatus IN (:serviceStatus) and token.status= :status and (token.createdOn BETWEEN :fromDate AND :toDate)")
    public List<TokenDTO> getAllPharmacyWaitingTokensByServiceId(@Param("serviceId") long serviceId, @Param("status") String status, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("serviceStatus") List<String> serviceStatus);

    @Query("SELECT new com.vm.qsmart2api.dtos.token.TokenDTO(token.tokenId, token.tokenNo, patient.mrnNo, concat(patient.lastName,' ',patient.firstName) as patientName, servBooked.startTime, token.createdOn, token.updatedOn,  room.roomId, room.roomNo, token.priority, token.appaitment.tranId, token.updatedOn) FROM TblServiceBooked servBooked "
            + " left join servBooked.appointment appt "
            + " left join TblToken token ON token.appaitment = appt.tranId "
            + " left join TblPatient patient ON patient.patientId= appt.patientId "
            + " left join TblRoom room ON room.roomId= token.roomNo "
            + " where servBooked.service.serviceId= :serviceId and servBooked.serviceStatus = :serviceStatus and token.status= :status and (token.createdOn BETWEEN :fromDate AND :toDate) and token.servedBy = :servedBy")
    public List<TokenDTO> getAllPharmacyServingTokensByServiceIdNdServedBy(@Param("serviceId") long serviceId, @Param("status") String status,
            @Param("servedBy") long servedBy, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("serviceStatus") String serviceStatus);
}
