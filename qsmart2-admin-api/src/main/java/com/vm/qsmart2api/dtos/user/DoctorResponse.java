/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.user;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class DoctorResponse {
    private List<DoctorDto> doctors;

    public DoctorResponse() {
    }

    public DoctorResponse(List<DoctorDto> doctors) {
        this.doctors = doctors;
    }

    public List<DoctorDto> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<DoctorDto> doctors) {
        this.doctors = doctors;
    }

    @Override
    public String toString() {
        return "DoctorResponse{" + "doctors=" + doctors + '}';
    }
    
    
}
