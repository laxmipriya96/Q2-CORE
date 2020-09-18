/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.repositary;

import com.vm.qsmart2.model.TblVisitSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Phani
 */
public interface VisitSummaryRepositary extends JpaRepository<TblVisitSummary, Long>{
    
     @Query("SELECT tbl FROM TblVisitSummary tbl WHERE tbl.transId= :transId")
     public TblVisitSummary findVisitSummaryObjByTranID(@Param("transId")String tranId);
    
}
