/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.scanner;


/**
 *
 * @author Tejasri
 */
public class ScannerCrtDto {
  
    private String scannerName;
    private String scannerIp;
    private Long branch;
    private int themeId;

    public ScannerCrtDto() {
    }

    public String getScannerName() {
        return scannerName;
    }

    public void setScannerName(String scannerName) {
        this.scannerName = scannerName;
    }

    public String getScannerIp() {
        return scannerIp;
    }

    public void setScannerIp(String scannerIp) {
        this.scannerIp = scannerIp;
    }

    public Long getBranch() {
        return branch;
    }

    public void setBranch(Long branch) {
        this.branch = branch;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    @Override
    public String toString() {
        return "ScannerCrtDto{" + "scannerName=" + scannerName + ", scannerIp=" + scannerIp + ", branch=" + branch + ", themeId=" + themeId + '}';
    }

}
