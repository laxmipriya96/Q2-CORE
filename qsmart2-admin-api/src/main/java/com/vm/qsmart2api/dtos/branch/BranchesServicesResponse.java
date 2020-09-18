/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.branch;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Phani
 */
public class BranchesServicesResponse {

    
    private List<BranchesServices> branches = new ArrayList<>();

    public List<BranchesServices> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchesServices> branches) {
        this.branches = branches;
    }

    public BranchesServicesResponse() {
    }

    public BranchesServicesResponse(List<BranchesServices> branches) {
        this.branches = branches;
    }

    @Override
    public String toString() {
        return "BranchesServicesResponse{" + "branches=" + branches + '}';
    }

    
    
}
