/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.repositary;

import com.vm.qsmart2.model.TblVisitDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Phani
 */
public interface VisitDetailsRepositary extends JpaRepository<TblVisitDetail, Long>{
    
    
    
}
