/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.template;

/**
 *
 * @author Phani
 */
public class SurveryRequest {
    
    private String mobileNumber;
    private String emailId;
    private String whatsappNumber;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }

    @Override
    public String toString() {
        return "SurveryRequest{" + "mobileNumber=" + mobileNumber + ", emailId=" + emailId + ", whatsappNumber=" + whatsappNumber + '}';
    }
    
}
