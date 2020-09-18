/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.global;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Phani
 */
@JsonIgnoreProperties( ignoreUnknown = true)
public class MailServerSettings {
    
    private String serverName;
    private String emailAccount;
    private String password;
    private String serverIp;
    private String mailBoxType;
    private int port;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMailBoxType() {
        return mailBoxType;
    }

    public void setMailBoxType(String mailBoxType) {
        this.mailBoxType = mailBoxType;
    }
    

    @Override
    public String toString() {
        return "MailServerSettings{" + "serverName=" + serverName + ", emailAccount=" + emailAccount + ", password=" + password + ", serverIp=" + serverIp + ", port=" + port + '}';
    }
    
}
