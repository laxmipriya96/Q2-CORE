/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.service;

import java.util.List;
import java.util.Set;

/**
 *
 * @author Tejasri
 */
public class ApptServicDto {

    private Set<ApptCreateDto> apptTypes;
    private Long serviceId;

    public ApptServicDto() {
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public ApptServicDto(Set<ApptCreateDto> apptTypes) {
        this.apptTypes = apptTypes;
    }

    public Set<ApptCreateDto> getApptTypes() {
        return apptTypes;
    }

    public void setApptTypes(Set<ApptCreateDto> apptTypes) {
        this.apptTypes = apptTypes;
    }

    @Override
    public String toString() {
        return "ApptServicDto{" + "apptTypes=" + apptTypes + ", serviceId=" + serviceId + '}';
    }

}
