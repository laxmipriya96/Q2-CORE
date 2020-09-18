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
public class PwdPolicySettings {
    
    private int minimumLength;
    private int maximumLength;
    private String specialCharactersAllowed;
    private boolean consecutiveDigitsAllowed;
    private int noOfUpperCaseLetters;
    private int noOfLowerCaseLetters;
  
    public PwdPolicySettings() {
    }

    public int getMinimumLength() {
        return minimumLength;
    }

    public void setMinimumLength(int minimumLength) {
        this.minimumLength = minimumLength;
    }

    public int getMaximumLength() {
        return maximumLength;
    }

    public void setMaximumLength(int maximumLength) {
        this.maximumLength = maximumLength;
    }

    public String getSpecialCharactersAllowed() {
        return specialCharactersAllowed;
    }

    public void setSpecialCharactersAllowed(String specialCharactersAllowed) {
        this.specialCharactersAllowed = specialCharactersAllowed;
    }

    public boolean isConsecutiveDigitsAllowed() {
        return consecutiveDigitsAllowed;
    }

    public void setConsecutiveDigitsAllowed(boolean consecutiveDigitsAllowed) {
        this.consecutiveDigitsAllowed = consecutiveDigitsAllowed;
    }

    public int getNoOfUpperCaseLetters() {
        return noOfUpperCaseLetters;
    }

    public void setNoOfUpperCaseLetters(int noOfUpperCaseLetters) {
        this.noOfUpperCaseLetters = noOfUpperCaseLetters;
    }

    public int getNoOfLowerCaseLetters() {
        return noOfLowerCaseLetters;
    }

    public void setNoOfLowerCaseLetters(int noOfLowerCaseLetters) {
        this.noOfLowerCaseLetters = noOfLowerCaseLetters;
    }

    @Override
    public String toString() {
        return "PwdPolicySettings{" + "minimumLength=" + minimumLength + ", maximumLength=" + maximumLength + ", specialCharactersAllowed=" + specialCharactersAllowed + ", consecutiveDigitsAllowed=" + consecutiveDigitsAllowed + ", noOfUpperCaseLetters=" + noOfUpperCaseLetters + ", noOfLowerCaseLetters=" + noOfLowerCaseLetters + '}';
    }
}
