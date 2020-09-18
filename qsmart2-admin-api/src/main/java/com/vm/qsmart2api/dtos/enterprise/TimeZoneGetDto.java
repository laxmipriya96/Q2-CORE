/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.enterprise;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class TimeZoneGetDto {
        private List<TimeZoneDto> timeZones;

    public TimeZoneGetDto() {
    }

    public TimeZoneGetDto(List<TimeZoneDto> timeZones) {
        this.timeZones = timeZones;
    }

    public List<TimeZoneDto> getTimeZones() {
        return timeZones;
    }

    public void setTimeZones(List<TimeZoneDto> timeZones) {
        this.timeZones = timeZones;
    }

    @Override
    public String toString() {
        return "TimeZoneGetDto{" + "timeZones=" + timeZones + '}';
    }

    
}
