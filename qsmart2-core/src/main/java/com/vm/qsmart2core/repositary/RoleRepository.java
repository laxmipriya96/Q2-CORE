/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.repositary;

import com.vm.qsmart2.model.TblRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author LENOVO
 */
public interface RoleRepository extends JpaRepository<TblRole, Long> {

    @Query("SELECT tbl.roleId FROM TblRole tbl WHERE tbl.roleCode = :roleCode")
    public Long findRoleIdByRoleCode(@Param("roleCode") String roleCode);
    
}
