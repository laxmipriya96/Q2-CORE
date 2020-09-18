/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.jwt;

/**
 *
 * @author Phani
 */
public class JwtEnterpriseDto {

    private long enterpriseId;
    private String enterpriseNameEn;
    private String enterpriseNameAr;

    public long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(long enterpriseId) {
        this.enterpriseId = enterpriseId;
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
    
    
}
