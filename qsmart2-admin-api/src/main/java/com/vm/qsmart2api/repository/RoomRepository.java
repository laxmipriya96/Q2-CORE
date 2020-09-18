/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblRoomMaster;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Tejasri
 */
public interface RoomRepository extends JpaRepository<TblRoomMaster, Long> {

    @Query("SELECT COUNT(tbl) FROM TblRoomMaster tbl WHERE UPPER(tbl.roomNameEn) = :roomNameEn and tbl.branchId = :branchId")
    public int validateRoomEngName(@Param("roomNameEn") String roomNameEn, @Param("branchId") Long branchId);

    @Query("SELECT COUNT(tbl) FROM TblRoomMaster tbl WHERE UPPER(tbl.roomNameAr) = :roomNameAr and tbl.branchId = :branchId")
    public int validateRoomNameArbInDb(@Param("roomNameAr") String roomNameAr, @Param("branchId") Long branchId);

    @Query("SELECT COUNT(tbl) FROM TblRoomMaster tbl WHERE UPPER(tbl.roomNameEn)= :roomNameEn and tbl.roomMasterId != :roomMasterId and tbl.branchId = :branchId")
    public int validateRoomEngNameByRoomId(@Param("roomNameEn") String roomNameEn, @Param("roomMasterId") Long roomMasterId, @Param("branchId") Long branchId);

    @Query("SELECT COUNT(tbl) FROM TblRoomMaster tbl WHERE UPPER(tbl.roomNameAr) = :roomNameAr and tbl.roomMasterId != :roomMasterId and tbl.branchId = :branchId")
    public int validateRoomArbNameByRoomId(@Param("roomNameAr") String roomNameAr, @Param("roomMasterId") Long roomMasterId, @Param("branchId") Long branchId);

//    @Query("SELECT tbl, r FROM TblRoomMaster tbl "
//             + "inner join TblRoom r on tbl.roomMasterId = r.roomMaster.roomMasterId "
//             + "where tbl.branchId = :branchId")
//            
//    public List<Object[]> findByBranchId(@Param("branchId") Long branchId);
    @Query("SELECT distinct tbl FROM TblRoomMaster tbl where tbl.branchId = :branchId")
    public List<TblRoomMaster> findByBranchId(@Param("branchId") Long branchId);

    @Query("SELECT tbl FROM TblRoomMaster tbl left join fetch tbl.rooms where tbl.roomMasterId = :roomMasterId")
    public TblRoomMaster findByRoomMasterId(@Param("roomMasterId") Long roomId);

//    @Query("SELECT distinct tbl FROM TblRoomMaster tbl join fetch tbl.users user where user.userId = :userId")
//    public List<Object[]> getMappingRoomsByUserId(@Param("userId") Long userId);
    @Query("SELECT COUNT(tbl) FROM TblRoomMaster tbl WHERE tbl.branchId= :branchId")
    public int countRoomsByBranchId(@Param("branchId") Long branchId);
}
