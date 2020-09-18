/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.enterprise;

/**
 *
 * @author Tejasri
 */
public class TimeZoneDto {
    
    private Long id;
    private String countryCode;
    private String countryName;
    private String timeZone;
    private String gmtOffset;

    public TimeZoneDto() {
    }

    public TimeZoneDto(Long id, String countryCode, String countryName, String timeZone, String gmtOffset) {
        this.id = id;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.timeZone = timeZone;
        this.gmtOffset = gmtOffset;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getGmtOffset() {
        return gmtOffset;
    }

    public void setGmtOffset(String gmtOffset) {
        this.gmtOffset = gmtOffset;
    }

    @Override
    public String toString() {
        return "TimeZoneDto{" + "id=" + id + ", countryCode=" + countryCode + ", countryName=" + countryName + ", timeZone=" + timeZone + ", gmtOffset=" + gmtOffset + '}';
    }
    
    
    
}
