/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import java.util.List;
import com.vm.qsmart2.model.TblRoom;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.vm.qsmart2api.dtos.roomNo.RoomDto;

/**
 *
 * @author Tejasri
 */
public interface RoomNoRepository extends JpaRepository<TblRoom, Long> {

    @Query("SELECT distinct tbl FROM TblRoom tbl where tbl.roomMaster.roomMasterId = :roomMasterId")
    public List<TblRoom> findByRoomMasterId(@Param("roomMasterId") Long roomMasterId);

    @Query("SELECT tbl FROM TblRoom tbl where tbl.roomMaster.roomMasterId = :roomMasterId and tbl.roomNo = :roomNO")
    public TblRoom findByRoomMasterIdAndRoomNo(@Param("roomMasterId") Long roomMasterId, @Param("roomNO") String roomNO);

    @Query("SELECT tbl FROM TblRoom tbl join fetch tbl.roomMaster where tbl.roomId = :roomId")
    public TblRoom findByRoomNoId(@Param("roomId") Long roomNoId);
    
    @Transactional
    @Modifying
    @Query("update TblRoom tbl set tbl.roomNo= :roomNo, tbl.updatedBy= :userId, tbl.updatedOn= :date where tbl.roomId= :roomId")
    public int updateRoom(@Param("roomId") Long roomId, @Param("roomNo") String roomNo, @Param("userId") long userId, @Param("date") Date date);

    @Query("SELECT new com.vm.qsmart2api.dtos.roomNo.RoomDto(room.roomId, room.roomNo, roommaster.roomNameEn, roommaster.roomNameAr) FROM TblRoom room "
            + "left join room.roomMaster roommaster "
            + "left join room.users user "
            + "where user.userId= :userId")
    public RoomDto getRoomByUserId(@Param("userId") Long userId);

}
