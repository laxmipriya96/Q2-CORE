/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.sms;

/**
 *
 * @author Ashok
 */
public class NotificationSmsDTO {

    private String templateName;
    private String mobileNo;
    private String smsTxt;
    private Long triggerId;

    /**
     * @return the templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName the templateName to set
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @return the mobileNo
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * @param mobileNo the mobileNo to set
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * @return the smsTxt
     */
    public String getSmsTxt() {
        return smsTxt;
    }

    /**
     * @param smsTxt the smsTxt to set
     */
    public void setSmsTxt(String smsTxt) {
        this.smsTxt = smsTxt;
    }

    /**
     * @return the triggerId
     */
    public Long getTriggerId() {
        return triggerId;
    }

    /**
     * @param triggerId the triggerId to set
     */
    public void setTriggerId(Long triggerId) {
        this.triggerId = triggerId;
    }
}
