/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.userprofile;

/**
 *
 * @author Ashok
 */
public class AssignedServiceGDto {

    private long serviceId;
    private String serviceNameEn;
    private String serviceNameAr;
    private long count;
    private long branchId;
    public AssignedServiceGDto() {
    }

    public AssignedServiceGDto(Long serviceId, String serviceNameEn, String serviceNameAr, long count, long branchId) {
        this.serviceId = serviceId;
        this.serviceNameEn = serviceNameEn;
        this.serviceNameAr = serviceNameAr;
        this.count = count;
        this.branchId = branchId;
    }

    public long getBranchId() {
        return branchId;
    }

    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }
    
    
    /**
     * @return the serviceId
     */
    public long getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId the serviceId to set
     */
    public void setServiceId(long serviceId) {
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
     * @return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(long count) {
        this.count = count;
    }

}
