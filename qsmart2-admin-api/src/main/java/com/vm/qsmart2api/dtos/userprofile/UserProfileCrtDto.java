/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.userprofile;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class UserProfileCrtDto {
    
    private Long branchId;
    private List<Long> services;

    public UserProfileCrtDto() {
    }

    public UserProfileCrtDto(Long branchId, List<Long> services) {
        this.branchId = branchId;
        this.services = services;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public List<Long> getServices() {
        return services;
    }

    public void setServices(List<Long> services) {
        this.services = services;
    }

    
}
