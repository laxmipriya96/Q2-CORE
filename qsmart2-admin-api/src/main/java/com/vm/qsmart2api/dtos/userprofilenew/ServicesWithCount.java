/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.userprofilenew;

/**
 *
 * @author Ashok
 */
public class ServicesWithCount {

    private Long serviceId;
    private String serviceNameEn;
    private String serviceNameAr;
    private Long count;

    public ServicesWithCount(Long serviceId, String serviceNameEn, String serviceNameAr) {
        this.serviceId = serviceId;
        this.serviceNameEn = serviceNameEn;
        this.serviceNameAr = serviceNameAr;
    }

    public ServicesWithCount(Long serviceId, String serviceNameEn, String serviceNameAr, Long count) {
        this.serviceId = serviceId;
        this.serviceNameEn = serviceNameEn;
        this.serviceNameAr = serviceNameAr;
        this.count = count;
    }

    public ServicesWithCount() {
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ServicesWithCount{" + "serviceId=" + serviceId + ", serviceNameEn=" + serviceNameEn + ", serviceNameAr=" + serviceNameAr + ", count=" + count + '}';
    }
    
    
}
