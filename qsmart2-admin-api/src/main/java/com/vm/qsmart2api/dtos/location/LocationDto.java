/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.location;

/**
 *
 * @author Tejasri
 */
public class LocationDto {
    private Long locationId;
    private String locationNameEn;
    private String locationNameAr;
    private String locationIdentfier;
    private String locationAddress;
    private int status;

    public LocationDto() {
    }

//    public LocationDto(Long locationId, String locationNameEn, String locationNameAr, String locationIdentfier, String locationAddress) {
//        this.locationId = locationId;
//        this.locationNameEn = locationNameEn;
//        this.locationNameAr = locationNameAr;
//        this.locationIdentfier = locationIdentfier;
//        this.locationAddress = locationAddress;
//    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "LocationDto{" + "locationId=" + locationId + ", locationNameEn=" + locationNameEn + ", locationNameAr=" + locationNameAr + ", locationIdentfier=" + locationIdentfier + ", locationAddress=" + locationAddress + '}';
    }
    
    
    
}
