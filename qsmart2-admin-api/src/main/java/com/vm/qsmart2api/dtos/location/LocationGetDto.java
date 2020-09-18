/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.location;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class LocationGetDto {
    
    private List<LocationDto> locations;

    public LocationGetDto() {
    }

    public LocationGetDto(List<LocationDto> locations) {
        this.locations = locations;
    }

    public List<LocationDto> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDto> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "LocationGetDto{" + "locations=" + locations + '}';
    }
    
    
    
}
