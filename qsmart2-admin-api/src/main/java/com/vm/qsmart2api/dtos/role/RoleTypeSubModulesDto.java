/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.role;

import com.vm.qsmart2api.dtos.menus.SubModulesDto;
import java.util.List;

/**
 *
 * @author Phani
 */
public class RoleTypeSubModulesDto {
    
    private String pagesType;
    
    private List<SubModulesDto> pages;

    public RoleTypeSubModulesDto() {
    }

    public RoleTypeSubModulesDto(String pagesType, List<SubModulesDto> pages) {
        this.pagesType = pagesType;
        this.pages = pages;
    }
    

    public String getPagesType() {
        return pagesType;
    }

    public void setPagesType(String pagesType) {
        this.pagesType = pagesType;
    }

    public List<SubModulesDto> getPages() {
        return pages;
    }

    public void setPages(List<SubModulesDto> pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "RoleTypeSubModulesDto{" + "pagesType=" + pagesType + ", pages=" + pages + '}';
    }
    
    
    
}
