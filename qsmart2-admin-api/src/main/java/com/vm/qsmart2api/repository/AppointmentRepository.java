/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Ashok
 */
public interface AppointmentRepository extends JpaRepository<TblAppointment, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE TblAppointment tbl  set tbl.apptStatus= :status WHERE tbl.appointmentId = :appointmentId")
    public int updateAppointment(@Param("appointmentId") long appointmentId, @Param("status") String status);

}
