/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.enterprise;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Phani
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Enterprise {
    
    private Integer id;
    private String enterpriseName;
    private String enterpriseArbName = "enterprise arabic name";
    private String enterpriseMail;
    private String enterpriseContact;
    private String enterpriseAddress;
    private String enterpriseWebsite;
    private String primaryName;
    private String primaryDesignation;
    private String primaryContact;
    private String primaryMobile;
    private String primaryMail;
    private String secondaryName;
    private String secondaryDesignation;
    private String secondaryContact;
    private String secondaryMobile;
    private String secondaryMail;

    public String getSecondaryMobile() {
        return secondaryMobile;
    }

    public void setSecondaryMobile(String secondaryMobile) {
        this.secondaryMobile = secondaryMobile;
    }

    public String getSecondaryMail() {
        return secondaryMail;
    }

    public void setSecondaryMail(String secondaryMail) {
        this.secondaryMail = secondaryMail;
    }
    
    
    
    private String licneseNumber;
    
    private String customer;
    
    private String licenseExpireDate;
    
    private String licenseAddedDate;
    
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    
    public String getLicenseAddedDate() {
        return licenseAddedDate;
    }

    public void setLicenseAddedDate(String licenseAddedDate) {
        this.licenseAddedDate = licenseAddedDate;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseMail() {
        return enterpriseMail;
    }

    public void setEnterpriseMail(String enterpriseMail) {
        this.enterpriseMail = enterpriseMail;
    }

    public String getEnterpriseContact() {
        return enterpriseContact;
    }

    public void setEnterpriseContact(String enterpriseContact) {
        this.enterpriseContact = enterpriseContact;
    }

    public String getEnterpriseAddress() {
        return enterpriseAddress;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        this.enterpriseAddress = enterpriseAddress;
    }

    public String getEnterpriseWebsite() {
        return enterpriseWebsite;
    }

    public void setEnterpriseWebsite(String enterpriseWebsite) {
        this.enterpriseWebsite = enterpriseWebsite;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public String getPrimaryDesignation() {
        return primaryDesignation;
    }

    public void setPrimaryDesignation(String primaryDesignation) {
        this.primaryDesignation = primaryDesignation;
    }

    public String getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(String primaryContact) {
        this.primaryContact = primaryContact;
    }

    public String getPrimaryMobile() {
        return primaryMobile;
    }

    public void setPrimaryMobile(String primaryMobile) {
        this.primaryMobile = primaryMobile;
    }

    public String getPrimaryMail() {
        return primaryMail;
    }

    public void setPrimaryMail(String primaryMail) {
        this.primaryMail = primaryMail;
    }

    public String getSecondaryName() {
        return secondaryName;
    }

    public void setSecondaryName(String secondaryName) {
        this.secondaryName = secondaryName;
    }

    public String getSecondaryDesignation() {
        return secondaryDesignation;
    }

    public void setSecondaryDesignation(String secondaryDesignation) {
        this.secondaryDesignation = secondaryDesignation;
    }

    public String getSecondaryContact() {
        return secondaryContact;
    }

    public void setSecondaryContact(String secondaryContact) {
        this.secondaryContact = secondaryContact;
    }

    public String getEnterpriseArbName() {
        return enterpriseArbName;
    }

    public void setEnterpriseArbName(String enterpriseArbName) {
        this.enterpriseArbName = enterpriseArbName;
    }

    public String getLicneseNumber() {
        return licneseNumber;
    }

    public void setLicneseNumber(String licneseNumber) {
        this.licneseNumber = licneseNumber;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getLicenseExpireDate() {
        return licenseExpireDate;
    }

    public void setLicenseExpireDate(String licenseExpireDate) {
        this.licenseExpireDate = licenseExpireDate;
    }

    @Override
    public String toString() {
        return "Enterprise{" + "id=" + id + ", enterpriseName=" + enterpriseName + ", enterpriseArbName=" + enterpriseArbName + ", enterpriseMail=" + enterpriseMail + ", enterpriseContact=" + enterpriseContact + ", enterpriseAddress=" + enterpriseAddress + ", enterpriseWebsite=" + enterpriseWebsite + ", primaryName=" + primaryName + ", primaryDesignation=" + primaryDesignation + ", primaryContact=" + primaryContact + ", primaryMobile=" + primaryMobile + ", primaryMail=" + primaryMail + ", secondaryName=" + secondaryName + ", secondaryDesignation=" + secondaryDesignation + ", secondaryContact=" + secondaryContact + ", licneseNumber=" + licneseNumber + ", customer=" + customer + ", licenseExpireDate=" + licenseExpireDate + '}';
    }
    
    
    
}
