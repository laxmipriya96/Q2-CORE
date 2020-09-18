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
 * @author Ashok
 */
public class UserApptType {
    private Long apptTypeId;
    private List<UserCtrDTO> users;

    /**
     * @return the apptTypeId
     */
    public Long getApptTypeId() {
        return apptTypeId;
    }

    /**
     * @param apptTypeId the apptTypeId to set
     */
    public void setApptTypeId(Long apptTypeId) {
        this.apptTypeId = apptTypeId;
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
