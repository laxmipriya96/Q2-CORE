/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.display;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;

/**
 *
 * @author Tejasri
 */
@JsonIgnoreProperties( ignoreUnknown = true)
public class DisplayUpDto {

    private Long displayId;
    private String displayName;
    private String displayIdentifier;
    private Integer status;
    private int themeId;

    private Set<Long> branches;

    private Set<Long> services;

    public DisplayUpDto() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getDisplayId() {
        return displayId;
    }

    public void setDisplayId(Long displayId) {
        this.displayId = displayId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayIdentifier() {
        return displayIdentifier;
    }

    public void setDisplayIdentifier(String displayIdentifier) {
        this.displayIdentifier = displayIdentifier;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public Set<Long> getServices() {
        return services;
    }

    public void setServices(Set<Long> services) {
        this.services = services;
    }

    public Set<Long> getBranches() {
        return branches;
    }

    public void setBranches(Set<Long> branches) {
        this.branches = branches;
    }
    
    @Override
    public String toString() {
        return "DisplayUpDto{" + "displayId=" + displayId + ", displayName=" + displayName + ", displayIdentifier=" + displayIdentifier + ", themeId=" + themeId + ", services=" + services + '}';
    }

}
