/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.location;

import com.vm.qsmart2api.dtos.service.ServiceCrtDto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tejasri
 */
public class BranchGetDto {

    private Long branchId;
    private String branchNameEn;
    private String branchNameAr;
    private String branchIdentifier;
    private String serviceLocation;

    private List<ServiceCrtDto> children = new ArrayList<>();

    public BranchGetDto() {
    }

    public void addService(ServiceCrtDto service) {
        children.add(service);
    }

    public BranchGetDto(Long branchId, String branchNameEn, String branchNameAr, String branchIdentifier, String serviceLocation, List<ServiceCrtDto> services) {
        this.branchId = branchId;
        this.branchNameEn = branchNameEn;
        this.branchNameAr = branchNameAr;
        this.branchIdentifier = branchIdentifier;
        this.serviceLocation = serviceLocation;
        this.children = services;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
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

    public List<ServiceCrtDto> getChildren() {
        return children;
    }

    public void setChildren(List<ServiceCrtDto> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "BranchGetDto{" + "branchId=" + branchId + ", branchNameEn=" + branchNameEn + ", branchNameAr=" + branchNameAr + ", branchIdentifier=" + branchIdentifier + ", serviceLocation=" + serviceLocation + ", children=" + children + '}';
    }

    

}
