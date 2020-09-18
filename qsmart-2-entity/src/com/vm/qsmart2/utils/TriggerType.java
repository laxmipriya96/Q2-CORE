/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.utils;

/**
 *
 * @author Ashok
 */
public enum TriggerType {

    AMSG("New Appointment Msg"), RMSG("Reminder Msg"), CMSG("Checkin Msg");

    private String value;

    private TriggerType(String status) {
        this.value = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
