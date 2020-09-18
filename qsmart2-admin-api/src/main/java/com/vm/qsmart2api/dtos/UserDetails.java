/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos;

import com.vm.qsmart2api.dtos.enterprise.EnterpriseGDto;
import com.vm.qsmart2api.dtos.location.LocationDto;
import com.vm.qsmart2api.dtos.role.RoleDTO;

/**
 *
 * @author Phani
 */
public class UserDetails {

    private String userName;
    private String firstName;
    private String lastName;
    private String hashPassword;
    private long userId;
    private boolean isUserProfile;
    private short isActive;
    private String emailId;
    private String contactNo;
    private String userType;
    private String resourceCode;
    private Short isFirstLogin;
    private EnterpriseGDto enterprise;
    private LocationDto location;
    private RoleDTO roles;

    public UserDetails() {
    }

    public short getIsActive() {
        return isActive;
    }

    public void setIsActive(short isActive) {
        this.isActive = isActive;
    }
    
    public UserDetails(String username, long userId) {
        this.userName = username;
//        this.password = password;
        this.userId = userId;
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

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public long getUserId() {
        return userId;
    }

    public boolean isIsUserProfile() {
        return isUserProfile;
    }

    public void setIsUserProfile(boolean isUserProfile) {
        this.isUserProfile = isUserProfile;
    }
    
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public Short getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(Short isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public EnterpriseGDto getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(EnterpriseGDto enterprise) {
        this.enterprise = enterprise;
    }

    public RoleDTO getRoles() {
        return roles;
    }

    public void setRoles(RoleDTO roles) {
        this.roles = roles;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    @Override
    public String toString() {
        return "UserDetails{" + "userName=" + userName + ", userId=" + userId + ", emailId=" + emailId + ", contactNo=" + contactNo + ", userType=" + userType + ", resourceCode=" + resourceCode + ", isFirstLogin=" + isFirstLogin + ", enterprise=" + enterprise + ", roles=" + roles + '}';
    }

}
