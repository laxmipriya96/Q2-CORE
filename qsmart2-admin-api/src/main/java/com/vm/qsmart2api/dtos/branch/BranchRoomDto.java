/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.branch;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class BranchRoomDto {
   private List<BranchRoom> branches;

    public BranchRoomDto() {
    }

    public BranchRoomDto(List<BranchRoom> branches) {
        this.branches = branches;
    }

    public List<BranchRoom> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchRoom> branches) {
        this.branches = branches;
    }

    @Override
    public String toString() {
        return "BranchRoomDto{" + "branches=" + branches + '}';
    }
   
   
}
