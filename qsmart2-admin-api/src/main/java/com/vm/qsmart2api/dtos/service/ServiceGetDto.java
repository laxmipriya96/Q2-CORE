/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.service;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class ServiceGetDto {
    private List<ServiceCrtDto> services;

    public ServiceGetDto() {
    }

    public ServiceGetDto(List<ServiceCrtDto> services) {
        this.services = services;
    }

    public List<ServiceCrtDto> getServices() {
        return services;
    }

    public void setServices(List<ServiceCrtDto> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return "ServiceGetDto{" + "services=" + services + '}';
    }
}
