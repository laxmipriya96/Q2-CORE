/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.repositary;

import com.vm.qsmart2.model.TblAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Phani
 */
public interface AppointmentRepositary extends JpaRepository<TblAppointment, Long>{
    
    @Transactional
    @Modifying
    @Query("UPDATE TblAppointment tbl  set tbl.apptStatus= :apptStatus WHERE tbl.tranId = :tranId")
    public int updateTblAppointmentStatus(@Param("tranId") String tranId, @Param("apptStatus") String apptStatus);
    
    
}
