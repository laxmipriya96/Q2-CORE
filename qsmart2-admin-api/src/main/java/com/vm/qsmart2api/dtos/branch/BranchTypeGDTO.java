/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.branch;

import java.util.List;

/**
 *
 * @author Ashok
 */
public class BranchTypeGDTO {
    
    private List<BranchTypeDTO> branchTypes;

    public BranchTypeGDTO() {
    }

    public BranchTypeGDTO(List<BranchTypeDTO> branchTypes) {
        this.branchTypes = branchTypes;
    }

    /**
     * @return the branchTypes
     */
    public List<BranchTypeDTO> getBranchTypes() {
        return branchTypes;
    }

    /**
     * @param branchTypes the branchTypes to set
     */
    public void setBranchTypes(List<BranchTypeDTO> branchTypes) {
        this.branchTypes = branchTypes;
    }
    
}
