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
public class ModulesDto {

    private Integer moduleId;
    private String moduleName;
    private Integer sequenceNo;
    private String moduleType;
    private String link;
    private String icon;
    
    private List<SubModulesDto> subModules= new ArrayList<>();
    
    public void addSubmodule(SubModulesDto subModule){
        this.subModules.add(subModule);
    }

    public List<SubModulesDto> getSubModules() {
        return subModules;
    }

    public void setSubModules(List<SubModulesDto> subModules) {
        this.subModules = subModules;
    }
    
    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "ModulesDto{" + "moduleId=" + moduleId + ", moduleName=" + moduleName + ", sequenceNo=" + sequenceNo + ", moduleType=" + moduleType + ", link=" + link + ", icon=" + icon + ", subModules=" + subModules + '}';
    }
    
    
}
