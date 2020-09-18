/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.service;

import java.util.List;

/**
 *
 * @author Ashok
 */
public class ServiceGUDTO {
    
    private List<ServiceUDTO> services;

    public ServiceGUDTO() {
    }

    public ServiceGUDTO(List<ServiceUDTO> services) {
        this.services = services;
    }

    /**
     * @return the services
     */
    public List<ServiceUDTO> getServices() {
        return services;
    }

    /**
     * @param services the services to set
     */
    public void setServices(List<ServiceUDTO> services) {
        this.services = services;
    }
    
}
