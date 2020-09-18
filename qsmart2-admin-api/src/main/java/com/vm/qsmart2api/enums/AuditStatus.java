/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.enums;

/**
 *
 * @author Ashok
 */
public enum AuditStatus {
    
    SUCCESS("SUCCESS"), FAILURE("FAILURE");

    private String msg;

    AuditStatus(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }
}
