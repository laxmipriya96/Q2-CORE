/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.enterprise;

import com.vm.qsmart2api.dtos.Response;

/**
 *
 * @author Phani
 */
public class LicenseDetails extends Response{

    public String licneseNumber = "20156464613";
    private String licenseExpireDate = "2025-12-31";
    private String licenseActivationDate = "2020-05-01";
    private String filePath;
    private String noOfFacilities = "Unlimited";
    private String noOfBranches = "Unlimited";
    private String noOfDoctors = "Unlimited";
    
    
    public LicenseDetails() {
    }

    public LicenseDetails(boolean status, int statusCode, String messages) {
        super(status, statusCode, messages);
    }

    public LicenseDetails(String licneseNumber, String licenseExpireDate, String licenseActivationDate, 
            String noOfFacilities, String noOfBranches, String noOfDoctors) {
        this.licneseNumber = licneseNumber;
        this.licenseExpireDate = licenseExpireDate;
        this.licenseActivationDate = licenseActivationDate;
        this.noOfFacilities = noOfFacilities;
        this.noOfBranches = noOfBranches;
        this.noOfDoctors = noOfDoctors;
    }

    public LicenseDetails(String licneseNumber, String licenseExpireDate, String licenseActivationDate, 
            String noOfFacilities, String noOfBranches, String noOfDoctors, boolean status, String messages) {
        super(status, messages);
        this.licneseNumber = licneseNumber;
        this.licenseExpireDate = licenseExpireDate;
        this.licenseActivationDate = licenseActivationDate;
        this.noOfFacilities = noOfFacilities;
        this.noOfBranches = noOfBranches;
        this.noOfDoctors = noOfDoctors;
    }

    public LicenseDetails(String licneseNumber, String licenseExpireDate, String licenseActivationDate, 
            String noOfFacilities, String noOfBranches, String noOfDoctors, boolean status, int statusCode, String messages) {
        super(status, statusCode, messages);
        this.licneseNumber = licneseNumber;
        this.licenseExpireDate = licenseExpireDate;
        this.licenseActivationDate = licenseActivationDate;
        this.noOfFacilities = noOfFacilities;
        this.noOfBranches = noOfBranches;
        this.noOfDoctors = noOfDoctors;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLicneseNumber() {
        return licneseNumber;
    }

    public void setLicneseNumber(String licneseNumber) {
        this.licneseNumber = licneseNumber;
    }

    public String getLicenseExpireDate() {
        return licenseExpireDate;
    }

    public void setLicenseExpireDate(String licenseExpireDate) {
        this.licenseExpireDate = licenseExpireDate;
    }

    public String getLicenseActivationDate() {
        return licenseActivationDate;
    }

    public void setLicenseActivationDate(String licenseActivationDate) {
        this.licenseActivationDate = licenseActivationDate;
    }

    public String getNoOfBranches() {
        return noOfBranches;
    }

    public void setNoOfBranches(String noOfBranches) {
        this.noOfBranches = noOfBranches;
    }

    public String getNoOfFacilities() {
        return noOfFacilities;
    }

    public void setNoOfFacilities(String noOfFacilities) {
        this.noOfFacilities = noOfFacilities;
    }

    public String getNoOfDoctors() {
        return noOfDoctors;
    }

    public void setNoOfDoctors(String noOfDoctors) {
        this.noOfDoctors = noOfDoctors;
    }
    
    

    @Override
    public String toString() {
        return "LicenseDetails{" + super.toString() + "licneseNumber=" + licneseNumber + ", licenseExpireDate=" + licenseExpireDate + ", licenseActivationDate=" + licenseActivationDate + ", filePath=" + filePath + ", noOfFacilities=" + noOfFacilities + ", noOfBranches=" + noOfBranches + ", noOfDoctors=" + noOfDoctors + '}';
    }

    
}
