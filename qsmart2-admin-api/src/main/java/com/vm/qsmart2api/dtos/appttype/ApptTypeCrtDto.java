/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.appttype;

/**
 *
 * @author Tejasri
 */
public class ApptTypeCrtDto {
    private String apptType;
    private String apptCode;

    public ApptTypeCrtDto() {
    }

    public ApptTypeCrtDto(String apptType, String apptCode) {
        this.apptType = apptType;
        this.apptCode = apptCode;
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
        return "ApptTypeCrtDto{" + "apptType=" + apptType + ", apptCode=" + apptCode + '}';
    }

   
    
}
