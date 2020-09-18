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
public class CustomRolesResponse {
    private List<CustomRoleDto> customRoles;

    public List<CustomRoleDto> getCustomRoles() {
        return customRoles;
    }

    public void setCustomRoles(List<CustomRoleDto> customRoles) {
        this.customRoles = customRoles;
    }

    public CustomRolesResponse() {
    }

    public CustomRolesResponse(List<CustomRoleDto> customRoles) {
        this.customRoles = customRoles;
    }
    
    
}
