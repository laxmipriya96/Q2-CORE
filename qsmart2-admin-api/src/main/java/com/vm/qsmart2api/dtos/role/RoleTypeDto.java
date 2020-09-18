/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.role;

/**
 *
 * @author Tejasri
 */
public class RoleTypeDto {
 private Integer roleTypeId;
 private String roleTypeCode;
 private String roleType;

    public RoleTypeDto() {
    }

    public RoleTypeDto(Integer roleTypeId, String roleTypeCode, String roleType) {
        this.roleTypeId = roleTypeId;
        this.roleType = roleType;
        this.roleTypeCode = roleTypeCode;
    }

    public String getRoleTypeCode() {
        return roleTypeCode;
    }

    public void setRoleTypeCode(String roleTypeCode) {
        this.roleTypeCode = roleTypeCode;
    }

    public Integer getRoleTypeId() {
        return roleTypeId;
    }

    public void setRoleTypeId(Integer roleTypeId) {
        this.roleTypeId = roleTypeId;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    @Override
    public String toString() {
        return "RoleTypeDto{" + "roleTypeId=" + roleTypeId + ", roleType=" + roleType + '}';
    }
 
 
    
}
