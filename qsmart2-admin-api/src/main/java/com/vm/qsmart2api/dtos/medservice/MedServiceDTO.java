/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.medservice;

/**
 *
 * @author Ashok
 */
public class MedServiceDTO {

    private String medServiceName;
    private String medServiceCode;

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

}
