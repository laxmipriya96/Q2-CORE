/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.menus;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Phani
 */
public class SubModulesDto {
   
    private Integer subModuleId;
    private String subModuleName;
    private Integer sequenceNo;
    private Integer branchType;
    private int readPermissionId;
    private String icon;
    private String link;
    
    private List<PermissionsDto> privileges = new ArrayList<>();
    
    public void addprivileges(PermissionsDto permission){
        privileges.add(permission);
    }

    public List<PermissionsDto> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PermissionsDto> privileges) {
        this.privileges = privileges;
    }

    public Integer getSubModuleId() {
        return subModuleId;
    }

    public void setSubModuleId(Integer subModuleId) {
        this.subModuleId = subModuleId;
    }

    public String getSubModuleName() {
        return subModuleName;
    }

    public void setSubModuleName(String subModuleName) {
        this.subModuleName = subModuleName;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Integer getBranchType() {
        return branchType;
    }

    public void setBranchType(Integer branchType) {
        this.branchType = branchType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getReadPermissionId() {
        return readPermissionId;
    }

    public void setReadPermissionId(int readPermissionId) {
        this.readPermissionId = readPermissionId;
    }
    
    @Override
    public String toString() {
        return "SubModulesDto{" + "subModuleId=" + subModuleId + ", subModuleName=" + subModuleName + ", sequenceNo=" + sequenceNo + ", branchType=" + branchType + ", icon=" + icon + ", link=" + link + ", privileges=" + privileges + '}';
    }

   
   
}
