/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.service;

/**
 *
 * @author Tejasri
 */
public class ServiceUpDto {
    
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
    private short isVital = 0;

    public short getIsVital() {
        return isVital;
    }

    public void setIsVital(short isVital) {
        this.isVital = isVital;
    }

    public ServiceUpDto() {
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceNameEn() {
        return serviceNameEn;
    }

    public void setServiceNameEn(String serviceNameEn) {
        this.serviceNameEn = serviceNameEn;
    }

    public String getServiceNameAr() {
        return serviceNameAr;
    }

    public void setServiceNameAr(String serviceNameAr) {
        this.serviceNameAr = serviceNameAr;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public int getStartSeq() {
        return startSeq;
    }

    public void setStartSeq(int startSeq) {
        this.startSeq = startSeq;
    }

    public int getEndSeq() {
        return endSeq;
    }

    public void setEndSeq(int endSeq) {
        this.endSeq = endSeq;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getWaitTimeAvg() {
        return waitTimeAvg;
    }

    public void setWaitTimeAvg(int waitTimeAvg) {
        this.waitTimeAvg = waitTimeAvg;
    }

    public int getMinCheckinTime() {
        return minCheckinTime;
    }

    public void setMinCheckinTime(int minCheckinTime) {
        this.minCheckinTime = minCheckinTime;
    }

    public int getMaxCheckinTime() {
        return maxCheckinTime;
    }

    public void setMaxCheckinTime(int maxCheckinTime) {
        this.maxCheckinTime = maxCheckinTime;
    }

    public short getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(short isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "ServiceUpDto{" + "serviceId=" + serviceId + ", serviceNameEn=" + serviceNameEn + ", serviceNameAr=" + serviceNameAr + ", tokenPrefix=" + tokenPrefix + ", startSeq=" + startSeq + ", endSeq=" + endSeq + ", branchId=" + branchId + ", waitTimeAvg=" + waitTimeAvg + ", minCheckinTime=" + minCheckinTime + ", maxCheckinTime=" + maxCheckinTime + ", isDefault=" + isDefault + '}';
    }
}
