/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.manualtoken;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vm.qsmart2.utils.DateUtils;
import java.util.Date;

/**
 *
 * @author Ashok
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManualTokenDTO {

    private String mrnNo;
    private String firstName;
    private String lastName;
    private Long branchId;
    private Long serviceId;
    private Long drId;
    private String appointmentTime;
    private int accompanyVisitors;

    public String getMrnNo() {
        return mrnNo;
    }

    public void setMrnNo(String mrnNo) {
        this.mrnNo = mrnNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getDrId() {
        return drId;
    }

    public void setDrId(Long drId) {
        this.drId = drId;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getAccompanyVisitors() {
        return accompanyVisitors;
    }

    public void setAccompanyVisitors(int accompanyVisitors) {
        this.accompanyVisitors = accompanyVisitors;
    }

    @Override
    public String toString() {
        return "ManualTokenDTO{" + "mrnNo=" + mrnNo + ", firstName=" + firstName + ", lastName=" + lastName + ", branchId=" + branchId + ", serviceId=" + serviceId + ", drId=" + drId + ", appointmentTime=" + appointmentTime + ", accompanyVisitors=" + accompanyVisitors + '}';
    }
    
}
