/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.appttype;

import java.util.List;

/**
 *
 * @author Tejasri
 */
 public class ApptTypeGetDto {
    
    private List<ApptTypeDto> apptTypes;

    public ApptTypeGetDto() {
    }

    public ApptTypeGetDto(List<ApptTypeDto> apptTypes) {
        this.apptTypes = apptTypes;
    }

    public List<ApptTypeDto> getApptTypes() {
        return apptTypes;
    }

    public void setApptTypes(List<ApptTypeDto> apptTypes) {
        this.apptTypes = apptTypes;
    }

    @Override
    public String toString() {
        return "ApptTypeGetDto{" + "apptTypes=" + apptTypes + '}';
    }
    
}
