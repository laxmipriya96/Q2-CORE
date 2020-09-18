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
public class LocationResponse {
    private String name;
    private List<LocationGDto> children;

    public LocationResponse() {
    }

    public LocationResponse(String name, List<LocationGDto> children) {
        this.name = name;
        this.children = children;
    }
    
    

    public List<LocationGDto> getChildren() {
        return children;
    }

    public void setChildren(List<LocationGDto> children) {
        this.children = children;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    @Override
    public String toString() {
        return "LocationResponse{" + "children=" + children + '}';
    }
    
}
