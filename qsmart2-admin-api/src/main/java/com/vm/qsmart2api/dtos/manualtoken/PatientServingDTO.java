/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.manualtoken;

/**
 *
 * @author Ashok
 */
public class PatientServingDTO {

    private String mrnNO;
    private String firstName;
    private String lastName;
    private String serviceNameEn;
    private Long serviceBookedId;
    private String status;

    public PatientServingDTO(String mrnNO, String firstName, String lastName, String serviceNameEn, Long serviceBookedId, String status) {
        this.mrnNO = mrnNO;
        this.firstName = firstName;
        this.lastName = lastName;
        this.serviceNameEn = serviceNameEn;
        this.serviceBookedId = serviceBookedId;
        this.status = status;
    }
    
    public String getMrnNO() {
        return mrnNO;
    }

    public void setMrnNO(String mrnNO) {
        this.mrnNO = mrnNO;
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

    public String getServiceNameEn() {
        return serviceNameEn;
    }

    public void setServiceNameEn(String serviceNameEn) {
        this.serviceNameEn = serviceNameEn;
    }

    public Long getServiceBookedId() {
        return serviceBookedId;
    }

    public void setServiceBookedId(Long serviceBookedId) {
        this.serviceBookedId = serviceBookedId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
