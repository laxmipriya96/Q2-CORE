/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.user;

import java.util.List;

/**
 *
 * @author ASHOK
 */
public class UserTypeGDto {
    
    private List<UserType> userType;

    public UserTypeGDto(List<UserType> userType) {
        this.userType = userType;
    }

    public UserTypeGDto() {
       
    }


    public List<UserType> getUserType() {
        return userType;
    }

    /**
     * @param userType the userType to set
     */
    public void setUserType(List<UserType> userType) {
        this.userType = userType;
    }
    
}
