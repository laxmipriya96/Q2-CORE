/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.role;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class RoleType {
    
    private List<RoleTypeDto> roleTypes;

    public RoleType() {
    }

    public RoleType(List<RoleTypeDto> roleTypes) {
        this.roleTypes = roleTypes;
    }

    public List<RoleTypeDto> getRoleTypes() {
        return roleTypes;
    }

    public void setRoleTypes(List<RoleTypeDto> roleTypes) {
        this.roleTypes = roleTypes;
    }

    @Override
    public String toString() {
        return "RoleType{" + "roleTypes=" + roleTypes + '}';
    }
}
