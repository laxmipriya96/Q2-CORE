/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.medservice;

import com.vm.qsmart2api.dtos.user.UserCtrDTO;
import java.util.List;

/**
 *
 * @author Ashok
 */
public class UserMedService {
    
    private Long medServiceId;
    private List<UserCtrDTO> users;

    /**
     * @return the medServiceId
     */
    public Long getMedServiceId() {
        return medServiceId;
    }

    /**
     * @param medServiceId the medServiceId to set
     */
    public void setMedServiceId(Long medServiceId) {
        this.medServiceId = medServiceId;
    }

    /**
     * @return the users
     */
    public List<UserCtrDTO> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(List<UserCtrDTO> users) {
        this.users = users;
    }
    
}
