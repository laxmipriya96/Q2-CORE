/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblDisplay;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Tejasri
 */
public interface DisplayRepository extends JpaRepository<TblDisplay, Long> {

    @Query("SELECT COUNT(tbl) FROM TblDisplay tbl WHERE UPPER(tbl.displayName) = :displayName and tbl.locationId = :locationId")
    public int validateDisplayName(@Param("displayName") String displayName, @Param("locationId") Long locationId);

    @Query("SELECT COUNT(tbl) FROM TblDisplay tbl WHERE UPPER(tbl.displayName) = :displayName and tbl.displayId != :displayId and tbl.locationId = :locationId")
    public int validateDisplayNameByDisplayId(@Param("displayName") String displayName, @Param("displayId") Long displayId, @Param("locationId") Long locationId);

    @Query("SELECT distinct tbl FROM TblDisplay tbl left join fetch tbl.services services where tbl.locationId = :locationId order by tbl.displayId desc")
    public List<TblDisplay> findByLocationId(@Param("locationId") Long locationId);

//    @Query(" SELECT tbl.displayId, tbl.displayIdentifier, tbl.displayName, tbl.themeId, theme.themeName, "
//            + " theme.themeType, theme.isDefault, tt.tokenId, tt.tokenNo, tr.roomId, tr.roomNo, "
//            + " rm.roomNameEn, rm.roomNameAr, tt.updatedOn    FROM TblDisplay tbl "
//            + " left join TblTheme theme on theme.themeId = tbl.themeId "
//            + " left join fetch tbl.services s"
//            + " left join fetch s.serviceBookings sb "
//            + " left join fetch sb.appointment apt "
//            + " left join TblToken tt on tt.appaitment.tranId = apt.tranId "
//            + " left join TblRoom tr on tr.roomId = tt.roomNo "
//            + " left join fetch tr.roomMaster rm "
//            + " where tbl.displayId = :displayId and sb.serviceStatus = :serviceStatus")
//    @Query(" SELECT tbl, theme, tt, tr FROM TblDisplay tbl "
//            + " left join TblTheme theme on theme.themeId = tbl.themeId "
//            + " left join fetch tbl.services s"
//            + " left join fetch s.serviceBookings sb "
//            + " left join fetch sb.appointment apt "
//            + " left join TblToken tt on tt.appaitment.tranId = apt.tranId and (tt.createdOn BETWEEN :fromDate AND :toDate)"
//            + " left join TblRoom tr on tr.roomId = tt.roomNo "
//            + " left join fetch tr.roomMaster rm "
//            + " where tbl.displayIdentifier = :displayIdentifier and sb.serviceStatus = :serviceStatus order by token.updatedOn DESC")
    @Query(" SELECT tbl.displayId, tbl.displayIdentifier, tbl.displayName, tbl.themeId, theme.themeName, "
            + " theme.themeType, theme.isDefault, tt.tokenId, tt.tokenNo, tr.roomId, tr.roomNo, "
            + " rm.roomNameEn, rm.roomNameAr, tt.updatedOn    FROM TblDisplay tbl "
            + " left join TblTheme theme on theme.themeId = tbl.themeId "
            + " left join tbl.services s "
            + " left join TblServiceBooked sb on sb.service.serviceId= s.serviceId and sb.serviceStatus = :serviceStatus "
            + " left join TblAppointment apt on apt.tranId= sb.appointment.tranId "
            + " left join TblToken tt on tt.appaitment.tranId = apt.tranId and (tt.createdOn BETWEEN :fromDate AND :toDate) "
            + " left join TblRoom tr on tr.roomId = tt.roomNo "
            + " left join TblRoomMaster rm on rm.roomMasterId= tr.roomMaster.roomMasterId "
            + " where tbl.displayIdentifier = :displayIdentifier order by tt.updatedOn DESC ")
    public List<Object[]> getDisplayInfoWithTokensAndRoomInfo(
            @Param("displayIdentifier") String displayId, 
            @Param("serviceStatus") String serviceStatus,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate);


}
