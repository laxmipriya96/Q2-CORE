/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblToken;
import java.util.Date;
import java.util.List;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Ashok
 */
public interface TokenRepository extends JpaRepository<TblToken, Long> {

    @Query("SELECT COUNT(tbl) FROM TblToken tbl where tbl.roomNo = :roomNo and tbl.status in(:status) and (tbl.createdOn BETWEEN :fromDate AND :toDate)")
    public int findNoOfTokensServingInRoom(@Param("roomNo") long roomNo, @Param("status") List<String> status, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT tbl FROM TblToken tbl where tbl.tokenId = :tokenId and tbl.status = :status")
    public TblToken findTokenById(@Param("tokenId") long tokenId, @Param("status") String status);
    
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT tbl FROM TblToken tbl where tbl.tokenId = :tokenId and tbl.status IN (:status)")
    public TblToken findTokenByIdWithStatusList(@Param("tokenId") long tokenId, @Param("status") List<String> statusList);

    
    @Query("SELECT tbl FROM TblToken tbl where tbl.appaitment.tranId = :tranId")
    public TblToken getTokenByTranId(@Param("tranId") String tranId);
}
