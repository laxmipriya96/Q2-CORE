/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.repositary;

import com.vm.qsmart2.model.TblHl7Cdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Phani
 */
public interface Hl7CdrRepositary extends JpaRepository<TblHl7Cdr, Long> {

    @Query("SELECT tbl.mrnNo FROM TblHl7Cdr tbl where tbl.hl7CdrId = :hl7CdrId ")
    public String findMrnByHl7CdrId(@Param("hl7CdrId") long hl7CdrId);

}
