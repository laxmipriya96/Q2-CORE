/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblService;
import com.vm.qsmart2.model.TblUserProfile;
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
public interface UserProfileRepository extends JpaRepository<TblUserProfile, Long> {

    @Query(" SELECT distinct serv FROM TblUserProfile tbl "
            + " left join TblService serv on serv.serviceId = tbl.serviceId "
            + " left join fetch serv.serviceBookings sb "
            + " left join fetch sb.appointment appt"
            + " where tbl.userId= :userId")
    public List<TblService> getAllServicesByUserId(@Param("userId") long userId);
    

    @Query("SELECT count(tbl) FROM TblUserProfile tbl where tbl.userId= :userId")
    public int getCountByUserId(@Param("userId") long userId);

    @Query("SELECT distinct room.roomId, room.roomNo, roommaster.roomNameEn, roommaster.roomNameAr, roommaster.maxAllowedToken FROM TblUserProfile tbl "
            + "left join TblBranch b on tbl.branchId = b.branchId "
            + "left join TblRoomMaster roommaster on roommaster.branchId = b.branchId "
            + "left join roommaster.rooms room "
            + "where tbl.userId= :userId")
    public List<Object[]> getRoomsByUserId(@Param("userId") Long userId);
    
    
    @Query("SELECT distinct room.roomId, room.roomNo, roommaster.roomNameEn, roommaster.roomNameAr, roommaster.maxAllowedToken FROM TblUserProfile tbl "
            + "left join TblBranch b on tbl.branchId = b.branchId "
            + "left join TblRoomMaster roommaster on roommaster.branchId = b.branchId "
            + "left join roommaster.rooms room "
            + "where tbl.userId = :userId and tbl.branchId = :branchId")
    public List<Object[]> getRoomsByUserIdNdBranchId(@Param("userId") Long userId, @Param("branchId") Long branchId);

    @Query("SELECT distinct b,rm FROM TblUserProfile tbl "
            + "left join TblBranch b on tbl.branchId = b.branchId "
            + "left join TblRoomMaster rm on rm.branchId = b.branchId "
            + "left join fetch rm.rooms r "
            + "where tbl.userId= :userId")
    public List<Object[]> getAllBranchesWithRoomsByUserId(@Param("userId") Long userId);

    @Query(" SELECT serv.serviceId, serv.serviceNameEn, serv.serviceNameAr, count(token.tokenId) FROM TblUserProfile tbl "
            + "left join TblService serv on serv.serviceId = tbl.serviceId "
            + "left join TblServiceBooked servbooked on servbooked.service.serviceId= serv.serviceId and servbooked.serviceStatus IN (:serviceStatus)"
            + "left join TblAppointment appt on appt.tranId= servbooked.appointment.tranId "
            + "left join TblToken token on token.appaitment.tranId= appt.tranId and token.status = :status and (token.createdOn BETWEEN :fromDate AND :toDate)"
            + "where tbl.userId= :userId "
            + "group by serv.serviceId, serv.serviceNameEn, serv.serviceNameAr")
    public List<Object[]> getAllServicesAndCountByUserId(@Param("userId") Long userId, @Param("status") String status, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("serviceStatus") List<String> serviceStatus);

    
    @Query(" SELECT serv.serviceId, serv.serviceNameEn, serv.serviceNameAr, count(token.tokenId) FROM TblUserProfile tbl "
            + "left join TblService serv on serv.serviceId = tbl.serviceId "
            + "left join TblServiceBooked servbooked on servbooked.service.serviceId= serv.serviceId "
            + "left join TblAppointment appt on appt.tranId= servbooked.appointment.tranId "
            + "left join TblToken token on token.appaitment.tranId= appt.tranId and token.status = :status "
            + "where tbl.userId= :userId and tbl.branchId = :branchId "
            + "group by serv.serviceId, serv.serviceNameEn, serv.serviceNameAr")
    public List<Object[]> getAllServicesAndCountByUserIdAndBranchId(@Param("userId") Long userId, @Param("branchId") Long branchId,  @Param("status") String status);
    
    
    @Modifying
    @Transactional
    @Query("delete from TblUserProfile tbl WHERE tbl.userId =:userId")
    public void deleteByUserIdInDb(@Param("userId") long userId);

//    @Query("SELECT new com.vm.qsmart2api.dtos.user.DoctorDto((u.userId) as drId, concat(u.lastName,' ',u.firstName) as doctorName, at.apptTypeId) FROM TblService service "
//            + " left join TblApptType at on at.apptTypeId = service.apptTypes.apptTypeId "
//            + " left join TblUser u on u.userId = at.users.userId "
//            + " left join TblRole r on r.roleId = u.roles.roleId "
//            + " where r.roleCode= :roleCode and service.serviceId= :serviceId")
//    public List<DoctorDto> getAllDoctorsByServiceId(@Param("roleCode") String roleCode,@Param("serviceId") long serviceId);
}
