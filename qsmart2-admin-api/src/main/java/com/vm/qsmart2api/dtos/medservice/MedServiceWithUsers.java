/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.medservice;

import java.util.List;

/**
 *
 * @author Ashok
 */
public class MedServiceWithUsers {
    
    private List<MedServiceGDTO> medServiceGDTO;

    /**
     * @return the medServiceGDTO
     */
    public List<MedServiceGDTO> getMedServiceGDTO() {
        return medServiceGDTO;
    }

    /**
     * @param medServiceGDTO the medServiceGDTO to set
     */
    public void setMedServiceGDTO(List<MedServiceGDTO> medServiceGDTO) {
        this.medServiceGDTO = medServiceGDTO;
    }

  
}
