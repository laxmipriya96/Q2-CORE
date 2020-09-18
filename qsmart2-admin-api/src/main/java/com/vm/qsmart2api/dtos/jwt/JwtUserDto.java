/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.jwt;

/**
 *
 * @author Phani
 */
public class JwtUserDto {
    private String userName;
    private String firstName;
    private String lastName;
    private long userId;
    private String userType;
    private JwtRoleDto roles;
    private JwtEnterpriseDto enterprise;
    private JWTLocationDto location;

    public JwtUserDto() {
    }
    

    public JwtUserDto(String userName, long userId) {
        this.userName = userName;
        this.userId = userId;
    }
    

    public JwtRoleDto getRoles() {
        return roles;
    }

    public void setRoles(JwtRoleDto roles) {
        this.roles = roles;
    }

    public JwtEnterpriseDto getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(JwtEnterpriseDto enterprise) {
        this.enterprise = enterprise;
    }

    public JWTLocationDto getLocation() {
        return location;
    }

    public void setLocation(JWTLocationDto location) {
        this.location = location;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    
}
