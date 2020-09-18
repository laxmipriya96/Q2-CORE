/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.kiosk;

import java.util.Set;

/**
 *
 * @author Phani
 */
public class KioskCrtDto {
    
    private String kioskName;
    private String kioskIp;
    private Set<Long> branches;
    private int themeId;

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }
    
    public String getKioskName() {
        return kioskName;
    }

    public void setKioskName(String kioskName) {
        this.kioskName = kioskName;
    }

    public String getKioskIp() {
        return kioskIp;
    }

    public void setKioskIp(String kioskIp) {
        this.kioskIp = kioskIp;
    }

    public Set<Long> getBranches() {
        return branches;
    }

    public void setBranches(Set<Long> branches) {
        this.branches = branches;
    }

    @Override
    public String toString() {
        return "KisokCrtDto{" + "kioskName=" + kioskName + ", kioskIp=" + kioskIp + ", branches=" + branches + '}';
    }
    
    
}
