/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.service;

import com.vm.qsmart2api.dtos.appttype.ApptTypeDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Ashok
 */
public class ServiceUDTO {

    private Long serviceId;
    private String serviceNameEn;
    private String serviceNameAr;
    private String tokenPrefix;
    private int startSeq;
    private int endSeq;
    private int branchId;
    private int waitTimeAvg;
    private int minCheckinTime;
    private int maxCheckinTime;
    private short isDefault;
    private short isVital;

    private Set<ApptCreateDto> apptTypes;
    
    public ServiceUDTO() {
    }

    public short getIsVital() {
        return isVital;
    }

    public void setIsVital(short isVital) {
        this.isVital = isVital;
    }
    
    /**
     * @return the serviceId
     */
    public Long getServiceId() {
        return serviceId;
    }

    public Set<ApptCreateDto> getApptTypes() {
        return apptTypes;
    }

    public void setApptTypes(Set<ApptCreateDto> apptTypes) {
        this.apptTypes = apptTypes;
    }

    
    
    /**
     * @param serviceId the serviceId to set
     */
    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @return the serviceNameEn
     */
    public String getServiceNameEn() {
        return serviceNameEn;
    }

    /**
     * @param serviceNameEn the serviceNameEn to set
     */
    public void setServiceNameEn(String serviceNameEn) {
        this.serviceNameEn = serviceNameEn;
    }

    /**
     * @return the serviceNameAr
     */
    public String getServiceNameAr() {
        return serviceNameAr;
    }

    /**
     * @param serviceNameAr the serviceNameAr to set
     */
    public void setServiceNameAr(String serviceNameAr) {
        this.serviceNameAr = serviceNameAr;
    }

    /**
     * @return the tokenPrefix
     */
    public String getTokenPrefix() {
        return tokenPrefix;
    }

    /**
     * @param tokenPrefix the tokenPrefix to set
     */
    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    /**
     * @return the startSeq
     */
    public int getStartSeq() {
        return startSeq;
    }

    /**
     * @param startSeq the startSeq to set
     */
    public void setStartSeq(int startSeq) {
        this.startSeq = startSeq;
    }

    /**
     * @return the endSeq
     */
    public int getEndSeq() {
        return endSeq;
    }

    /**
     * @param endSeq the endSeq to set
     */
    public void setEndSeq(int endSeq) {
        this.endSeq = endSeq;
    }

    /**
     * @return the branchId
     */
    public int getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the waitTimeAvg
     */
    public int getWaitTimeAvg() {
        return waitTimeAvg;
    }

    /**
     * @param waitTimeAvg the waitTimeAvg to set
     */
    public void setWaitTimeAvg(int waitTimeAvg) {
        this.waitTimeAvg = waitTimeAvg;
    }

    /**
     * @return the minCheckinTime
     */
    public int getMinCheckinTime() {
        return minCheckinTime;
    }

    /**
     * @param minCheckinTime the minCheckinTime to set
     */
    public void setMinCheckinTime(int minCheckinTime) {
        this.minCheckinTime = minCheckinTime;
    }

    /**
     * @return the maxCheckinTime
     */
    public int getMaxCheckinTime() {
        return maxCheckinTime;
    }

    /**
     * @param maxCheckinTime the maxCheckinTime to set
     */
    public void setMaxCheckinTime(int maxCheckinTime) {
        this.maxCheckinTime = maxCheckinTime;
    }

    /**
     * @return the isDefault
     */
    public short getIsDefault() {
        return isDefault;
    }
    /**
     * @param isDefault the isDefault to set
     */
    public void setIsDefault(short isDefault) {
        this.isDefault = isDefault;
    }
}
