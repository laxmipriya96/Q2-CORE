/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.medservice;

import com.vm.qsmart2api.dtos.user.UserCtrDTO;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Ashok
 */
public class MedServiceGDTO {
    
    private Long medServiceId;
    private String medServiceName;
    private String medServiceCode;
    private Set<UserCtrDTO> users;

    /**
     * @return the medServiceId
     */
    public Long getMedServiceId() {
        return medServiceId;
    }

    /**
     * @param medServiceId the medServiceId to set
     */
    public void setMedServiceId(Long medServiceId) {
        this.medServiceId = medServiceId;
    }

    /**
     * @return the medServiceName
     */
    public String getMedServiceName() {
        return medServiceName;
    }

    /**
     * @param medServiceName the medServiceName to set
     */
    public void setMedServiceName(String medServiceName) {
        this.medServiceName = medServiceName;
    }

    /**
     * @return the medServiceCode
     */
    public String getMedServiceCode() {
        return medServiceCode;
    }

    /**
     * @param medServiceCode the medServiceCode to set
     */
    public void setMedServiceCode(String medServiceCode) {
        this.medServiceCode = medServiceCode;
    }

    /**
     * @return the users
     */
    public Set<UserCtrDTO> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(Set<UserCtrDTO> users) {
        this.users = users;
    }

}
