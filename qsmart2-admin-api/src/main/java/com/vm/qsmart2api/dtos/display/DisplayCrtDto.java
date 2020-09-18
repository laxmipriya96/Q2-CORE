/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.display;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;
import org.apache.logging.log4j.core.util.Integers;

/**
 *
 * @author Tejasri
 */
@JsonIgnoreProperties( ignoreUnknown = true)
public class DisplayCrtDto {
    
    private String displayName;
    private String displayIdentifier;
    private int themeId;
    
    private Set<Long> services;

    public DisplayCrtDto() {
    }

    public DisplayCrtDto( String displayName, String displayIdentifier, int themeId) {
        this.displayName = displayName;
        this.displayIdentifier = displayIdentifier;
        this.themeId = themeId;
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

    @Override
    public String toString() {
        return "DisplayCrtDto{" + "displayName=" + displayName + ", displayIdentifier=" + displayIdentifier + ", themeId=" + themeId + ", services=" + services + '}';
    }
    
    
   
}
