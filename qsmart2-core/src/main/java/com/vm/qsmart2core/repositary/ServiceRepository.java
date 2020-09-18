/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.repositary;

import com.vm.qsmart2.model.TblService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Ashok
 */
public interface ServiceRepository extends JpaRepository<TblService, Long> {

    @Query("SELECT tbl FROM TblService tbl where tbl.branchId= :branchId and tbl.isDefault = :isDefault")
    public TblService getDefaultServiceByBranchId(@Param("branchId") long branchId, @Param("isDefault") short isDefault);

    
   
}
