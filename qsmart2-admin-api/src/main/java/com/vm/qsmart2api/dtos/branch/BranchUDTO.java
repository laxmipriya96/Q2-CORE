/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.branch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author ASHOK
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BranchUDTO {
    
    private Long branchId;
    private String branchNameEn;
    private String branchNameAr;
    private String branchIdentifier;
    private String serviceLocation;
    private Long branchTypeId;
    private short isRegistration = 0;
    private short isFloating = 0;
    private short isVital= 0;

    public short getIsVital() {
        return isVital;
    }

    public void setIsVital(short isVital) {
        this.isVital = isVital;
    }

    
    
    public short getIsRegistration() {
        return isRegistration;
    }

    public void setIsRegistration(short isRegistration) {
        this.isRegistration = isRegistration;
    }

    public short getIsFloating() {
        return isFloating;
    }

    public void setIsFloating(short isFloating) {
        this.isFloating = isFloating;
    }

    /**
     * @return the branchId
     */
    public Long getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the branchNameEn
     */
    public String getBranchNameEn() {
        return branchNameEn;
    }

    /**
     * @param branchNameEn the branchNameEn to set
     */
    public void setBranchNameEn(String branchNameEn) {
        this.branchNameEn = branchNameEn;
    }

    /**
     * @return the branchNameAr
     */
    public String getBranchNameAr() {
        return branchNameAr;
    }

    /**
     * @param branchNameAr the branchNameAr to set
     */
    public void setBranchNameAr(String branchNameAr) {
        this.branchNameAr = branchNameAr;
    }

    /**
     * @return the branchIdentifier
     */
    public String getBranchIdentifier() {
        return branchIdentifier;
    }

    /**
     * @param branchIdentifier the branchIdentifier to set
     */
    public void setBranchIdentifier(String branchIdentifier) {
        this.branchIdentifier = branchIdentifier;
    }

    /**
     * @return the serviceLocation
     */
    public String getServiceLocation() {
        return serviceLocation;
    }

    /**
     * @param serviceLocation the serviceLocation to set
     */
    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    /**
     * @return the branchTypeId
     */
    public Long getBranchTypeId() {
        return branchTypeId;
    }

    /**
     * @param branchTypeId the branchTypeId to set
     */
    public void setBranchTypeId(Long branchTypeId) {
        this.branchTypeId = branchTypeId;
    }
    
}
