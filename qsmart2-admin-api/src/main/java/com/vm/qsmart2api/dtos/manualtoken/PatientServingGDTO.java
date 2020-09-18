/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.manualtoken;

import com.vm.qsmart2api.dtos.Response;
import java.util.List;

/**
 *
 * @author Ashok
 */
public class PatientServingGDTO extends Response{
    private List<PatientServingDTO> PatientServingDTOs;

    public PatientServingGDTO() {
    }

    
    public PatientServingGDTO(List<PatientServingDTO> PatientServingDTOs) {
        this.PatientServingDTOs = PatientServingDTOs;
    }

    
    public List<PatientServingDTO> getPatientServingDTOs() {
        return PatientServingDTOs;
    }

    public void setPatientServingDTOs(List<PatientServingDTO> PatientServingDTOs) {
        this.PatientServingDTOs = PatientServingDTOs;
    }
    
}
