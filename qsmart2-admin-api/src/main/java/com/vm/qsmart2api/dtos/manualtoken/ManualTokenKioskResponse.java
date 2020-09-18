/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.manualtoken;

import com.vm.qsmart2api.dtos.Response;

/**
 *
 * @author Ashok
 */
public class ManualTokenKioskResponse extends Response {

    private Long tokenId;
    private String tokenNo;
    private String tokenTemplate;
    private String mrnNo;

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenTemplate() {
        return tokenTemplate;
    }

    public void setTokenTemplate(String tokenTemplate) {
        this.tokenTemplate = tokenTemplate;
    }

    public String getMrnNo() {
        return mrnNo;
    }

    public void setMrnNo(String mrnNo) {
        this.mrnNo = mrnNo;
    }

    @Override
    public String toString() {
        return "ManualTokenResponse{" + super.toString() + "tokenId=" + tokenId + ", mrnNo=" + mrnNo + '}';
    }

}
