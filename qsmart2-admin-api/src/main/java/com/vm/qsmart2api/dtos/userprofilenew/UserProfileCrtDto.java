/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.userprofilenew;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class UserProfileCrtDto {
    
    private List<UserProfileCrtBranch> branches;

    public UserProfileCrtDto() {
    }
    
    public List<UserProfileCrtBranch> getBranches() {
        return branches;
    }

    public void setBranches(List<UserProfileCrtBranch> branches) {
        this.branches = branches;
    }

    @Override
    public String toString() {
        return "UserProfileCrtDto{" + "branches=" + branches + '}';
    }
    
    

    
}
