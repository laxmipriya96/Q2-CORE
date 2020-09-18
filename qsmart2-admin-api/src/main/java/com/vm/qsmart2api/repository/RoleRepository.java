/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblRole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author LENOVO
 */
public interface RoleRepository extends JpaRepository<TblRole, Long> {

    @Query("SELECT distinct tbl FROM TblRole tbl left join fetch tbl.permissions  WHERE tbl.roleCode = :roleCode")
    public List<TblRole> findRoleByRoleCode(@Param("roleCode") String roleCode);
    
    @Query("SELECT tbl FROM TblRole tbl left join fetch tbl.users users WHERE tbl.roleId = :roleId")
    public TblRole findRoleByRoleId(@Param("roleId") long roleId);

    @Query("SELECT distinct tbl FROM TblRole tbl WHERE tbl.roleCode Not in (:roleCodes) and (tbl.isCustomRole = :isCustomRole1 or (tbl.isCustomRole = :isCustomRole2 and tbl.enterpriseId = :enterpriseId))")
    public List<TblRole> findCustomANdSystemRolesByRoleCode(@Param("roleCodes") List<String> roleCode, @Param("isCustomRole1") short isCustomRole1, @Param("isCustomRole2") short isCustomRole2, @Param("enterpriseId") int enterpriseId);

    @Query("SELECT distinct tbl FROM TblRole tbl WHERE tbl.roleCode != :roleCode and tbl.isCustomRole = :isCustomRole and tbl.enterpriseId = :enterpriseId")
    public List<TblRole> findCustomRolesByEnterpriseId(@Param("roleCode") String roleCode, @Param("isCustomRole") short customRole, @Param("enterpriseId") int enterpriseId);

    
    @Query("SELECT distinct tbl FROM TblRole tbl left join fetch tbl.permissions WHERE (tbl.isCustomRole = :isCustomRole  or (tbl.isCustomRole = :isCustomRole1 and tbl.enterpriseId = :enterpriseId)) and tbl.roleCode NOT IN (:rolecodes)")
    public List<TblRole> getCustomRolesByEnterpriseId(@Param("isCustomRole") short isCustomRole, @Param("isCustomRole1") short isCustomRole1, @Param("enterpriseId") int enterpriseId, @Param("rolecodes") List<String> rolecodes);
    
    
//    @Query("SELECT distinct tbl FROM TblRole tbl WHERE  and tbl.roleCode IN (:rolecodes)")
//    public List<TblRole> getCustomRolesByEnterpriseId(@Param("rolecodes") List<String> rolecodes);
    
    @Query("SELECT COUNT(tbl) FROM TblRole tbl WHERE UPPER(tbl.roleName)= :roleName and tbl.enterpriseId = :enterpriseId")
    public int validateRoleNameInDb(@Param("roleName") String roleName, @Param("enterpriseId") int enterpriseId);
    
    @Query("SELECT COUNT(tbl) FROM TblRole tbl WHERE UPPER(tbl.roleName)= :roleName and tbl.enterpriseId = :enterpriseId and tbl.roleId != :roleId")
    public int validateRoleNameInDbByID(@Param("roleName") String roleName, @Param("enterpriseId") int enterpriseId, @Param("roleId") long roleId);
}
