/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.enterprise;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Phani
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnterpriseCreateRequest {
    
    private String enterpriseNameEn;
    private String enterpriseNameAr;
    private Integer timeZoneId;
    private Integer languageId;
    private String logoPath;
    private String licenseFile;
    private Byte[] logo;

    public Byte[] getLogo() {
        return logo;
    }

    public void setLogo(Byte[] logo) {
        this.logo = logo;
    }
    
    public EnterpriseCreateRequest() {
    }

    public EnterpriseCreateRequest(String enterpriseNameEn, String enterpriseNameAr, Integer timeZoneId, Integer languageId, String logoPath, String licenseFile) {
        this.enterpriseNameEn = enterpriseNameEn;
        this.enterpriseNameAr = enterpriseNameAr;
        this.timeZoneId = timeZoneId;
        this.languageId = languageId;
        this.logoPath = logoPath;
        this.licenseFile = licenseFile;
    }

    public String getEnterpriseNameEn() {
        return enterpriseNameEn;
    }

    public void setEnterpriseNameEn(String enterpriseNameEn) {
        this.enterpriseNameEn = enterpriseNameEn;
    }

    public String getEnterpriseNameAr() {
        return enterpriseNameAr;
    }

    public void setEnterpriseNameAr(String enterpriseNameAr) {
        this.enterpriseNameAr = enterpriseNameAr;
    }

    public Integer getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(Integer timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getLicenseFile() {
        return licenseFile;
    }

    public void setLicenseFile(String licenseFile) {
        this.licenseFile = licenseFile;
    }

    @Override
    public String toString() {
        return "EnterpriseCreateRequest{" + "enterpriseNameEn=" + enterpriseNameEn + ", enterpriseNameAr=" + enterpriseNameAr + ", timeZoneId=" + timeZoneId + ", languageId=" + languageId + ", logoPath=" + logoPath + ", licenseFile=" + licenseFile + '}';
    }
    
}
