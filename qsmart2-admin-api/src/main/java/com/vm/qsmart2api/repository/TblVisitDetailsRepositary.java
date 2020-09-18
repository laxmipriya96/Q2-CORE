/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblVisitDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Phani
 */
public interface TblVisitDetailsRepositary extends JpaRepository<TblVisitDetail, Long> {

    @Query("SELECT tbl FROM TblVisitDetail tbl where tbl.transId = :transId and tbl.status = :status order by tbl.branchId desc")
    public TblVisitDetail findByTranIdAndStatus(@Param("transId") String transId, @Param("status") String status);

}
