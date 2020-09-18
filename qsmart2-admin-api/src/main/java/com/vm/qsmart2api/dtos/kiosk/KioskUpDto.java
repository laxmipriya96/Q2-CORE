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
public class KioskUpDto {

    private Long kioskId;
    private String kioskName;
    private String kioskIp;
    private String kioskIdentifier;
    private Integer status;
    private Set<Long> branches;
    private int themeId = 1;

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public Long getKioskId() {
        return kioskId;
    }

    public void setKioskId(Long kioskId) {
        this.kioskId = kioskId;
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

    public String getKioskIdentifier() {
        return kioskIdentifier;
    }

    public void setKioskIdentifier(String kioskIdentifier) {
        this.kioskIdentifier = kioskIdentifier;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Set<Long> getBranches() {
        return branches;
    }

    public void setBranches(Set<Long> branches) {
        this.branches = branches;
    }

    @Override
    public String toString() {
        return "KioskUpDto{" + "kioskId=" + kioskId + ", kioskName=" + kioskName + ", kioskIp=" + kioskIp + ", kioskIdentifier=" + kioskIdentifier + ", status=" + status + ", branches=" + branches + '}';
    }

    
    
}
