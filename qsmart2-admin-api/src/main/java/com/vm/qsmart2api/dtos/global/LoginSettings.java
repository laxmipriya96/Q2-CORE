/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.global;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Tejasri
 */
@JsonIgnoreProperties( ignoreUnknown = true)
public class LoginSettings {
    
    private int maxIncorrectLoginAttempts;
    private int accountUnlocksAfterXMinutes;
    private int dontAllowPreviousXPasswords;

    public LoginSettings() {
    }

    public int getMaxIncorrectLoginAttempts() {
        return maxIncorrectLoginAttempts;
    }

    public void setMaxIncorrectLoginAttempts(int maxIncorrectLoginAttempts) {
        this.maxIncorrectLoginAttempts = maxIncorrectLoginAttempts;
    }

    public int getAccountUnlocksAfterXMinutes() {
        return accountUnlocksAfterXMinutes;
    }

    public void setAccountUnlocksAfterXMinutes(int accountUnlocksAfterXMinutes) {
        this.accountUnlocksAfterXMinutes = accountUnlocksAfterXMinutes;
    }

    public int getDontAllowPreviousXPasswords() {
        return dontAllowPreviousXPasswords;
    }

    public void setDontAllowPreviousXPasswords(int dontAllowPreviousXPasswords) {
        this.dontAllowPreviousXPasswords = dontAllowPreviousXPasswords;
    }

    @Override
    public String toString() {
        return "LoginSettings{" + "maxIncorrectLoginAttempts=" + maxIncorrectLoginAttempts + ", accountUnlocksAfterXMinutes=" + accountUnlocksAfterXMinutes + ", dontAllowPreviousXPasswords=" + dontAllowPreviousXPasswords + '}';
    }
}
