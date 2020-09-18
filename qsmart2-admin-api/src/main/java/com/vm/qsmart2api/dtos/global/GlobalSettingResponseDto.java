/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.global;

import com.vm.qsmart2api.dtos.Response;

/**
 *
 * @author Phani
 */

public class GlobalSettingResponseDto extends Response {
    
    private Object data;
    
    private MailServerSettings mailSettings;
    private ActiveDirectorySettings adSettings;
    private PwdPolicySettings pwdSettings;
    private LoginSettings loginSettigns;
    private SmsSettings smsSettings;

    public GlobalSettingResponseDto() {
    }

    public GlobalSettingResponseDto(Object data, boolean status, int statusCode, String messages) {
        super(status, statusCode, messages);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public MailServerSettings getMailSettings() {
        return mailSettings;
    }

    public void setMailSettings(MailServerSettings mailSettings) {
        this.mailSettings = mailSettings;
    }

    public ActiveDirectorySettings getAdSettings() {
        return adSettings;
    }

    public void setAdSettings(ActiveDirectorySettings adSettings) {
        this.adSettings = adSettings;
    }

    public PwdPolicySettings getPwdSettings() {
        return pwdSettings;
    }

    public void setPwdSettings(PwdPolicySettings pwdSettings) {
        this.pwdSettings = pwdSettings;
    }

    public LoginSettings getLoginSettigns() {
        return loginSettigns;
    }

    public void setLoginSettigns(LoginSettings loginSettigns) {
        this.loginSettigns = loginSettigns;
    }

    public SmsSettings getSmsSettings() {
        return smsSettings;
    }

    public void setSmsSettings(SmsSettings smsSettings) {
        this.smsSettings = smsSettings;
    }
    
    

    @Override
    public String toString() {
        return "GlobalSettingResponseDto{" + super.toString()+ "data=" + data + '}';
    }
    
         
}
