/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.branch;

import com.vm.qsmart2api.dtos.service.ServiceCrtDto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Phani
 */
public class BranchesServices {

    private Long branchId;
    private String branchNameEn;
    private String branchNameAr;
    private String branchIdentifier;
    private String serviceLocation;
    private Long branchTypeId;
    private short isRegistration = 0;
    private short isFloating = 0;
    private short isVital= 0;
    
    private List<ServiceCrtDto> services = new ArrayList<>();

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }
    
    public void addService(ServiceCrtDto service) {
        services.add(service);
    }

    public String getBranchNameEn() {
        return branchNameEn;
    }

    public void setBranchNameEn(String branchNameEn) {
        this.branchNameEn = branchNameEn;
    }

    public String getBranchNameAr() {
        return branchNameAr;
    }

    public void setBranchNameAr(String branchNameAr) {
        this.branchNameAr = branchNameAr;
    }

    public String getBranchIdentifier() {
        return branchIdentifier;
    }

    public void setBranchIdentifier(String branchIdentifier) {
        this.branchIdentifier = branchIdentifier;
    }

    public String getServiceLocation() {
        return serviceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    public Long getBranchTypeId() {
        return branchTypeId;
    }

    public void setBranchTypeId(Long branchTypeId) {
        this.branchTypeId = branchTypeId;
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

    public List<ServiceCrtDto> getServices() {
        return services;
    }

    public void setServices(List<ServiceCrtDto> services) {
        this.services = services;
    }  

    public short getIsVital() {
        return isVital;
    }

    public void setIsVital(short isVital) {
        this.isVital = isVital;
    }
    
    
    
    @Override
    public String toString() {
        return "BranchesServices{" + "branchId=" + branchId + ", branchNameEn=" + branchNameEn + ", branchNameAr=" + branchNameAr + ", branchIdentifier=" + branchIdentifier + ", serviceLocation=" + serviceLocation + ", branchTypeId=" + branchTypeId + ", isRegistration=" + isRegistration + ", isFloating=" + isFloating + ", services=" + services + '}';
    }
    
}
