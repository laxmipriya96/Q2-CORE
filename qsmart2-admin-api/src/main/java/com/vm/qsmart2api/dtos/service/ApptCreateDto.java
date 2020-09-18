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
public class ApptCreateDto {
    private Long apptTypeId;
    private String apptType;
    private String apptCode;

    public ApptCreateDto() {
    }

    public ApptCreateDto(Long apptTypeId, String apptType, String apptCode) {
        this.apptTypeId = apptTypeId;
        this.apptType = apptType;
        this.apptCode = apptCode;
    }

    public Long getApptTypeId() {
        return apptTypeId;
    }

    public void setApptTypeId(Long apptTypeId) {
        this.apptTypeId = apptTypeId;
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

    @Override
    public String toString() {
        return "ApptCreateDto{" + "apptTypeId=" + apptTypeId + ", apptType=" + apptType + ", apptCode=" + apptCode + '}';
    }


}
