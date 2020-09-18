/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.text.StrSubstitutor;

/**
 *
 * @author Ashok
 */
public class TriggerParams {

    private String MRN;
    private String NAME;
    private String CONTACTNO;
    private String MAILADDRESS;
    private String LOCATION;
    private String BRANCH;
    private String SERVICE;
    private String APP_TIME;
    private String DOCTOR_NAME;
    private String CHECKIN_TIME;
    private String TOKEN_NO;
    private Long enterpriseId;

    public TriggerParams() {
    }

    public TriggerParams(String MRN, String NAME, String CONTACTNO, String MAILADDRESS, String LOCATION, String BRANCH, String SERVICE, Long enterpriseId) {
        this.MRN = MRN;
        this.NAME = NAME;
        this.CONTACTNO = CONTACTNO;
        this.MAILADDRESS = MAILADDRESS;
        this.LOCATION = LOCATION;
        this.BRANCH = BRANCH;
        this.SERVICE = SERVICE;
        this.enterpriseId = enterpriseId;
    }

    public TriggerParams(String MRN, String NAME, String CONTACTNO, String MAILADDRESS, String LOCATION, String BRANCH, String SERVICE, Date APP_TIME, String DOCTOR_NAME, Long enterpriseId) {
        this.MRN = MRN;
        this.NAME = NAME;
        this.CONTACTNO = CONTACTNO;
        this.MAILADDRESS = MAILADDRESS;
        this.LOCATION = LOCATION;
        this.BRANCH = BRANCH;
        this.SERVICE = SERVICE;
        this.APP_TIME = DateUtils.sdf.get().format(APP_TIME);
        this.DOCTOR_NAME = DOCTOR_NAME;
        this.enterpriseId = enterpriseId;
    }

    public TriggerParams(String MRN, String NAME, String CONTACTNO, String MAILADDRESS, String LOCATION, String BRANCH, String SERVICE, Date APP_TIME, String DOCTOR_NAME, Date CHECKIN_TIME, String TOKEN_NO, Long enterpriseId) {
        this.MRN = MRN;
        this.NAME = NAME;
        this.CONTACTNO = CONTACTNO;
        this.MAILADDRESS = MAILADDRESS;
        this.LOCATION = LOCATION;
        this.BRANCH = BRANCH;
        this.SERVICE = SERVICE;
        this.APP_TIME = DateUtils.sdf.get().format(APP_TIME);
        this.DOCTOR_NAME = DOCTOR_NAME;
        this.CHECKIN_TIME = DateUtils.sdf.get().format(CHECKIN_TIME);
        this.TOKEN_NO = TOKEN_NO;
        this.enterpriseId = enterpriseId;
    }

    public String getMRN() {
        return MRN;
    }

    public void setMRN(String MRN) {
        this.MRN = MRN;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getCONTACTNO() {
        return CONTACTNO;
    }

    public void setCONTACTNO(String CONTACTNO) {
        this.CONTACTNO = CONTACTNO;
    }

    public String getMAILADDRESS() {
        return MAILADDRESS;
    }

    public void setMAILADDRESS(String MAILADDRESS) {
        this.MAILADDRESS = MAILADDRESS;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public String getBRANCH() {
        return BRANCH;
    }

    public void setBRANCH(String BRANCH) {
        this.BRANCH = BRANCH;
    }

    public String getSERVICE() {
        return SERVICE;
    }

    public void setSERVICE(String SERVICE) {
        this.SERVICE = SERVICE;
    }

    public String getAPP_TIME() {
        return APP_TIME;
    }

    public void setAPP_TIME(String APP_TIME) {
        this.APP_TIME = APP_TIME;
    }

    public String getDOCTOR_NAME() {
        return DOCTOR_NAME;
    }

    public void setDOCTOR_NAME(String DOCTOR_NAME) {
        this.DOCTOR_NAME = DOCTOR_NAME;
    }

    public String getCHECKIN_TIME() {
        return CHECKIN_TIME;
    }

    public void setCHECKIN_TIME(String CHECKIN_TIME) {
        this.CHECKIN_TIME = CHECKIN_TIME;
    }

    public String getTOKEN_NO() {
        return TOKEN_NO;
    }

    public void setTOKEN_NO(String TOKEN_NO) {
        this.TOKEN_NO = TOKEN_NO;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
    
     public static String getTriggerTemplate(TriggerParams triggerParams, String template) {
        Map<String, String> map = new HashMap() {
            {
                put("MRN", triggerParams.getMRN());
            }

            {
                put("NAME", triggerParams.getNAME());
            }

            {
                put("CONTACTNO", triggerParams.getCONTACTNO());
            }

            {
                put("MAILADDRESS", triggerParams.getMAILADDRESS());
            }

            {
                put("LOCATION", triggerParams.getLOCATION());
            }

            {
                put("BRANCH", triggerParams.getBRANCH());
            }

            {
                put("SERVICE", triggerParams.getSERVICE());
            }

            {
                put("APP_TIME", triggerParams.getAPP_TIME());
            }

            {
                put("DOCTOR_NAME", triggerParams.getDOCTOR_NAME());
            }

            {
                put("CHECKIN_TIME", triggerParams.getCHECKIN_TIME());
            }

            {
                put("TOKEN_NO", triggerParams.getTOKEN_NO());
            }
        };
        template = StrSubstitutor.replace(template, map, "<$", "$>");
        return template;
    }
}
