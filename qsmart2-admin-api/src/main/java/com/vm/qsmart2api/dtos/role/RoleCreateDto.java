/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.role;    

import java.util.List;

/**
 *
 * @author Phani
 */
public class RoleCreateDto {
    
    private String roleName;
    private String roleCode;
    private int roleType;
    private int branchType;
    private short isCustomRole;
    
    private List<Integer> permissions;

    public short getIsCustomRole() {
        return isCustomRole;
    }

    public void setIsCustomRole(short isCustomRole) {
        this.isCustomRole = isCustomRole;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public int getBranchType() {
        return branchType;
    }

    public void setBranchType(int branchType) {
        this.branchType = branchType;
    }


    public List<Integer> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Integer> permissions) {
        this.permissions = permissions;
    }

}
