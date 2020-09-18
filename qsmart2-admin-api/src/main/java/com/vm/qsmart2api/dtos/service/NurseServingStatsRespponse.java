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
public class NurseServingStatsRespponse {
    List<NurseServingStats> serviceDetails;

    public NurseServingStatsRespponse() {
    }

    public NurseServingStatsRespponse(List<NurseServingStats> serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public List<NurseServingStats> getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(List<NurseServingStats> serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    @Override
    public String toString() {
        return "ServiceDetails{" + "serviceDetails=" + serviceDetails + '}';
    }
    
}
