/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.location;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tejasri
 */
public class LocationGDto {
    private Long locationId;
    private String locationNameEn;
    private String locationNameAr;
    private String locationIdentfier;
    private String locationAddress;
    private int status;
    
     private List<BranchGetDto> children = new ArrayList<>();

    public LocationGDto() {
    }

    public LocationGDto(Long locationId, String locationNameEn, String locationNameAr, String locationIdentfier, String locationAddress, int status) {
        this.locationId = locationId;
        this.locationNameEn = locationNameEn;
        this.locationNameAr = locationNameAr;
        this.locationIdentfier = locationIdentfier;
        this.locationAddress = locationAddress;
        this.status = status;
    }
     
      public void addBranch(BranchGetDto branch) {
        children.add(branch);
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationNameEn() {
        return locationNameEn;
    }

    public void setLocationNameEn(String locationNameEn) {
        this.locationNameEn = locationNameEn;
    }

    public String getLocationNameAr() {
        return locationNameAr;
    }

    public void setLocationNameAr(String locationNameAr) {
        this.locationNameAr = locationNameAr;
    }

    public String getLocationIdentfier() {
        return locationIdentfier;
    }

    public void setLocationIdentfier(String locationIdentfier) {
        this.locationIdentfier = locationIdentfier;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<BranchGetDto> getChildren() {
        return children;
    }

    public void setChildren(List<BranchGetDto> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "LocationGDto{" + "locationId=" + locationId + ", locationNameEn=" + locationNameEn + ", locationNameAr=" + locationNameAr + ", locationIdentfier=" + locationIdentfier + ", locationAddress=" + locationAddress + ", status=" + status + ", children=" + children + '}';
    }

}
