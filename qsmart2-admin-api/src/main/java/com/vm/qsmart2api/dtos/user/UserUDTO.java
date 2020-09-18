/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author ASHOK
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUDTO {

    private long userId;
    private String userName;
//    private String hashPassword;
    private String firstName;
    private String lastName;
    private String emailId;
    private String contactNo;
    private String userType;
    private String resourceCode;
    private long locationId;
    private long roleId;
    private long enterpriseId;
    private short isActive;
    private short systemAccess;
    private short isFirstLogin;

    public short getIsActive() {
        return isActive;
    }

    public void setIsActive(short isActive) {
        this.isActive = isActive;
    }
    
    

    public long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public short getSystemAccess() {
        return systemAccess;
    }

    public void setSystemAccess(short systemAccess) {
        this.systemAccess = systemAccess;
    }

    public short getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(short isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }
    
    
    
    /**
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the emailId
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * @param emailId the emailId to set
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     * @return the contactNo
     */
    public String getContactNo() {
        return contactNo;
    }

    /**
     * @param contactNo the contactNo to set
     */
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

   

    /**
     * @return the resourceCode
     */
    public String getResourceCode() {
        return resourceCode;
    }

    /**
     * @param resourceCode the resourceCode to set
     */
    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    

}
