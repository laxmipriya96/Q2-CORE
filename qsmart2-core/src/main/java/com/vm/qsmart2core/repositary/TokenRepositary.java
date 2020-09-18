/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.repositary;

import com.vm.qsmart2.model.TblToken;
import java.util.List;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Phani
 */
public interface TokenRepositary extends JpaRepository<TblToken, Long>{
    
    
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT tbl FROM TblToken tbl left join fetch tbl.appaitment appt where appt.tranId = :tranId ")
    public TblToken findTokenByTranId(@Param("tranId") String tranId);
    
    
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT tbl FROM TblToken tbl left join fetch tbl.appaitment appt where appt.tranId = :tranId and "
            + "tbl.status In (:status)")
    public TblToken findTokenByTranIdAndStatuss(@Param("tranId") String tranId, @Param("status") List<String> status);
    
}
