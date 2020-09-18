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
public class ServiceCrtDto {
    
    private Long serviceId;
    private Long branchId;
    private String serviceNameEn;
    private String serviceNameAr;

    public ServiceCrtDto() {
    }

    public ServiceCrtDto(Long serviceId, Long branchId, String serviceNameEn, String serviceNameAr) {
        this.serviceId = serviceId;
        this.branchId = branchId;
        this.serviceNameEn = serviceNameEn;
        this.serviceNameAr = serviceNameAr;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
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

    @Override
    public String toString() {
        return "ServiceCrtDto{" + "serviceId=" + serviceId + ", branchId=" + branchId + ", serviceNameEn=" + serviceNameEn + ", serviceNameAr=" + serviceNameAr + '}';
    }

    
 
}
