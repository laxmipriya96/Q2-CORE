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
public class ScannerUpDto {
    private Long scannerId;
    private String scannerIdentifier;
    private String scannerName;
    private String scannerIp;
    private Integer status;
    private Long branch;
    private int themeId = 1;

    public ScannerUpDto() {
    }

    public Long getScannerId() {
        return scannerId;
    }

    public void setScannerId(Long scannerId) {
        this.scannerId = scannerId;
    }

    public String getScannerIdentifier() {
        return scannerIdentifier;
    }

    public void setScannerIdentifier(String scannerIdentifier) {
        this.scannerIdentifier = scannerIdentifier;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        return "ScannerUpDto{" + "scannerId=" + scannerId + ", scannerIdentifier=" + scannerIdentifier + ", scannerName=" + scannerName + ", scannerIp=" + scannerIp + ", status=" + status + ", branch=" + branch + ", themeId=" + themeId + '}';
    }


}
