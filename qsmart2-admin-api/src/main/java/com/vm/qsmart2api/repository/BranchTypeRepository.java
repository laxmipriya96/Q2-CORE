/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblBranchType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Ashok
 */
public interface BranchTypeRepository extends JpaRepository<TblBranchType, Long>{
    
}
