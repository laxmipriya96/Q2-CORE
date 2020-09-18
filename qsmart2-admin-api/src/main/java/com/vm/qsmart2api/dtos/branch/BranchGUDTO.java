/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.branch;

import java.util.List;

/**
 *
 * @author ASHOK
 */
public class BranchGUDTO {
    
private List<BranchUDTO> branches;

    
    /**
     * @return the branches
     */
    public List<BranchUDTO> getBranches() {
        return branches;
    }

    public BranchGUDTO() {
    }

    public BranchGUDTO(List<BranchUDTO> branches) {
        this.branches = branches;
    }

    /**
     * @param branches the branches to set
     */
    public void setBranches(List<BranchUDTO> branches) {
        this.branches = branches;
    }
    
}
