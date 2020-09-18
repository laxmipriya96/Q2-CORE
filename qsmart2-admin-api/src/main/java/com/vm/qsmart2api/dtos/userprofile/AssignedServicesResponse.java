/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.userprofile;

import com.vm.qsmart2api.dtos.userprofile.AssignedServiceGDto;
import java.util.List;

/**
 *
 * @author Ashok
 */
public class AssignedServicesResponse {
    
    private Long branchId;
    private String branchName;

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    
    private List<AssignedServiceGDto> services;

//    public AssignedServicesResponse(List<AssignedServiceGDto> services) {
//       this.services= services;
//    }

    public AssignedServicesResponse() {
       
    }

    public AssignedServicesResponse(Long branchId, List<AssignedServiceGDto> services) {
        this.branchId = branchId;
        this.services = services;
    }
    
    

    /**
     * @return the services
     */
    public List<AssignedServiceGDto> getServices() {
        return services;
    }

    /**
     * @param services the services to set
     */
    public void setServices(List<AssignedServiceGDto> services) {
        this.services = services;
    }
}
