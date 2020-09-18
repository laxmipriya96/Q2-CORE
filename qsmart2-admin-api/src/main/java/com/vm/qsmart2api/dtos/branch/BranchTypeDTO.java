/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.branch;

/**
 *
 * @author Ashok
 */
public class BranchTypeDTO {
    
    private Long branchTypeId;
    private String branchTypeDisplay;
    private String branchTypeCode;

    /**
     * @return the branchTypeId
     */
    public Long getBranchTypeId() {
        return branchTypeId;
    }

    /**
     * @param branchTypeId the branchTypeId to set
     */
    public void setBranchTypeId(Long branchTypeId) {
        this.branchTypeId = branchTypeId;
    }

    /**
     * @return the branchTypeDisplay
     */
    public String getBranchTypeDisplay() {
        return branchTypeDisplay;
    }

    /**
     * @param branchTypeDisplay the branchTypeDisplay to set
     */
    public void setBranchTypeDisplay(String branchTypeDisplay) {
        this.branchTypeDisplay = branchTypeDisplay;
    }

    /**
     * @return the branchTypeCode
     */
    public String getBranchTypeCode() {
        return branchTypeCode;
    }

    /**
     * @param branchTypeCode the branchTypeCode to set
     */
    public void setBranchTypeCode(String branchTypeCode) {
        this.branchTypeCode = branchTypeCode;
    }
    
}
