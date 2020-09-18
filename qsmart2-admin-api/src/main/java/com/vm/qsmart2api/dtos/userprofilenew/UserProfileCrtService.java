/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.userprofilenew;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Phani
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfileCrtService {

    private Long serviceId;
    private String serviceEngName;
    private String serviceArbName;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceEngName() {
        return serviceEngName;
    }

    public void setServiceEngName(String serviceEngName) {
        this.serviceEngName = serviceEngName;
    }

    public String getServiceArbName() {
        return serviceArbName;
    }

    public void setServiceArbName(String serviceArbName) {
        this.serviceArbName = serviceArbName;
    }

    @Override
    public String toString() {
        return "UserProfileServices{" + "serviceId=" + serviceId + ", serviceEngName=" + serviceEngName + ", serviceArbName=" + serviceArbName + '}';
    }
    
}
