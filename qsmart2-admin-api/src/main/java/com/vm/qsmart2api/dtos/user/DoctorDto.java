/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.user;

/**
 *
 * @author Tejasri
 */
public class DoctorDto {
    
    private Long drId;
    private String doctorName;
    private String userName;
    private short isActive;
    
    public DoctorDto() {
    }

    public Long getDrId() {
        return drId;
    }

    public void setDrId(Long drId) {
        this.drId = drId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public short getIsActive() {
        return isActive;
    }

    public void setIsActive(short isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "DoctorDto{" + "drId=" + drId + ", doctorName=" + doctorName + ", userName=" + userName + ", isActive=" + isActive + '}';
    }
    
}
