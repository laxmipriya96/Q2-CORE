/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.repositary;

import com.vm.qsmart2.model.TblTokenSequence;
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
@Transactional
public interface TokenSequenceRepositary extends JpaRepository<TblTokenSequence, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT tbl FROM TblTokenSequence tbl where tbl.serviceId = :serviceId ")
    public TblTokenSequence getTokenSequenceByServiceId(@Param("serviceId") long serviceId);
    
   
}
