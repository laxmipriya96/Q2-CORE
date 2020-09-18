/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.role;

import java.util.List;

/**
 *
 * @author ASHOK
 */
public class RoleGDTO {
    private List<RoleDTO> roles;

    public RoleGDTO() {
    }
    
    public RoleGDTO(List<RoleDTO> roles) {
        this.roles = roles;
    }

    /**
     * @return the roles
     */
    public List<RoleDTO> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "RoleGDTO{" + "roles=" + roles + '}';
    }
    
    
}
