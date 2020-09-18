/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.user;

/**
 *
 * @author ASHOK
 */
public class UserType {

    private String  userTypeId;
    
    private String type;

    public UserType(String id, String type) {
        this.userTypeId = id;
        this.type = type;
    }

    public UserType() {
    }

    public String getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(String userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserType{" + "userTypeId=" + userTypeId + ", type=" + type + '}';
    }
    
}
