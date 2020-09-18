/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.appttype;

import com.vm.qsmart2api.dtos.user.UserCtrDTO;
import java.util.List;

/**
 *
 * @author Tejasri
 */
public class ApptTypeDto {
    private Long apptTypeId;
    private Long locationId;
    private String apptType;
    private String apptCode;
    private List<UserCtrDTO> users;

    public ApptTypeDto() {
    }

    public ApptTypeDto(Long apptTypeId, Long locationId, String apptType, String apptCode) {
        this.apptTypeId = apptTypeId;
        this.locationId = locationId;
        this.apptType = apptType;
        this.apptCode = apptCode;
    }

    public Long getApptTypeId() {
        return apptTypeId;
    }

    public void setApptTypeId(Long apptTypeId) {
        this.apptTypeId = apptTypeId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getApptType() {
        return apptType;
    }

    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

    public String getApptCode() {
        return apptCode;
    }

    public void setApptCode(String apptCode) {
        this.apptCode = apptCode;
    }

    public List<UserCtrDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserCtrDTO> users) {
        this.users = users;
    }
    
    @Override
    public String toString() {
        return "ApptTypeDto{" + "apptTypeId=" + apptTypeId + ", locationId=" + locationId + ", apptType=" + apptType + ", apptCode=" + apptCode + '}';
    }

}