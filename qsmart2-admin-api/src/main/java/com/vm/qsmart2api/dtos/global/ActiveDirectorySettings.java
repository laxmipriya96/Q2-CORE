

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.global;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Phani
 */
@JsonIgnoreProperties( ignoreUnknown = true)
public class ActiveDirectorySettings {
    private String host;
    private int port;
    private String userName;
    private String password;
    private String adPath;
    private int ldapTimeOut;
    private int maxUsers;
    
    private String firstName;
    private String lastName;
    private String mobileNo;
    private String mailId;
    private String loginId;
    
    public ActiveDirectorySettings() {
    }
    
    
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdPath() {
        return adPath;
    }

    public void setAdPath(String adPath) {
        this.adPath = adPath;
    }

    public int getLdapTimeOut() {
        return ldapTimeOut;
    }

    public void setLdapTimeOut(int ldapTimeOut) {
        this.ldapTimeOut = ldapTimeOut;
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

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    @Override
    public String toString() {
        return "ActiveDirectorySettings{" + "host=" + host + ", port=" + port + ", userName=" + userName + ", password=" + password + ", adPath=" + adPath + ", ldapTimeOut=" + ldapTimeOut + ", maxUsers=" + maxUsers + ", firstName=" + firstName + ", lastName=" + lastName + ", mobileNo=" + mobileNo + ", mailId=" + mailId + ", loginId=" + loginId + '}';
    }
           
}
