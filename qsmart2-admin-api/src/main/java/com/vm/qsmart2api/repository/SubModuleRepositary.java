/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblSubModule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Phani
 */
public interface SubModuleRepositary extends JpaRepository<TblSubModule, Long>{
    
    @Query("SELECT distinct tbl FROM TblSubModule tbl join fetch tbl.permissions permissions join fetch permissions.module pm where UPPER(pm.moduleType) = :moduleType")
    public List<TblSubModule> getSubModulesByRoleType(@Param("moduleType") String moduleType);
    
    
    @Query("SELECT distinct tbl FROM TblSubModule tbl join fetch tbl.permissions permissions join fetch permissions.module pm where UPPER(pm.moduleType) = :moduleType and tbl.branchType = :branchType")
    public List<TblSubModule> getSubModulesByRoleTypeNdBranchType(@Param("moduleType") String moduleType, @Param("branchType") int branchType);
}
