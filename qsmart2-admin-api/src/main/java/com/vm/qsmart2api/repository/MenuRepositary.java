/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblPermission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Phani
 */
public interface MenuRepositary extends JpaRepository<TblPermission, Integer> {

    @Query("SELECT distinct tbl FROM TblPermission tbl join fetch tbl.module module join fetch tbl.subModule subModule join fetch tbl.roles roles where roles.roleId = :roleId order by module.sequenceNo ASC, subModule.sequenceNo ASC")
    public List<TblPermission> getAllPermissionsByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT distinct tbl FROM TblPermission tbl join fetch tbl.module md join fetch tbl.subModule sm where UPPER(md.moduleType) = :moduleType and sm.branchType = :branchType order by sm.sequenceNo ASC")
    public List<TblPermission> getSubModulesByRoleTypeNdBranchType(@Param("moduleType") String moduleType, @Param("branchType") int branchType);

    @Query("SELECT distinct tbl FROM TblPermission tbl join fetch tbl.module md join fetch tbl.subModule sm where UPPER(md.moduleType) = :moduleType and UPPER(sm.subModuleName) != :subModuleName order by sm.sequenceNo ASC")
    public List<TblPermission> getSubModulesByRoleTypeNdSubmoduleName(@Param("moduleType") String moduleType, @Param("subModuleName") String subModuleName);

    @Query("SELECT distinct tbl FROM TblPermission tbl join fetch tbl.module md join fetch tbl.subModule sm where UPPER(md.moduleType) = :moduleType order by sm.sequenceNo ASC")
    public List<TblPermission> getSubModulesByRoleType(@Param("moduleType") String moduleType);
}
