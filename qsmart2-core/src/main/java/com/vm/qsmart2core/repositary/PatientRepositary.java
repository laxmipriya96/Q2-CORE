/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.repositary;

import com.vm.qsmart2.model.TblPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Phani
 */
public interface PatientRepositary extends JpaRepository<TblPatient, Long>{
    
    @Query("SELECT tbl.patientId FROM TblPatient tbl where tbl.mrnNo = :mrnNo ")
    public Long findPatientIdByMrn(@Param("mrnNo") String mrnNo);
    
    
    @Query("SELECT tbl FROM TblPatient tbl where tbl.mrnNo = :mrnNo ")
    public TblPatient findPatientByMrn(@Param("mrnNo") String mrnNo);
    
}
