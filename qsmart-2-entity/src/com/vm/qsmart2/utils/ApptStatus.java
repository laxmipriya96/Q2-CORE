/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.utils;

/**
 *
 * @author Phani
 */
public enum ApptStatus {

    SCHEDULED("SCHEDULED"),
    HL7CHECKIN("HL7CHECKIN"),
    KIOSKCHECKIN("KIOSKCHECKIN"),
    MANUALCHECKIN("MANUALCHECKIN"),
    
    WAITING("WAITING"),
    
    CANCELLED("CANCELLED"),
    NURSECHECKOUT("NURSECHECKOUT"),
    HL7CHECKOUT("HL7CHECKOUT"),
    FORCECHECKOUT("FORCECHECKOUT"),
    SERVING("SERVING"),
    FLOTING("FLOTING");

    private String value;

    public static ApptStatus getInstce(String value) {
        switch (value) {
            case "SCHEDULED":
                return SCHEDULED;
            case "HL7CHECKIN":
                return HL7CHECKIN;
            case "KIOSKCHECKIN":
                return KIOSKCHECKIN;
            case "MANUALCHECKIN":
                return MANUALCHECKIN;
            case "WAITING":
                return WAITING;
            case "CANCELLED":
                return CANCELLED;
            case "NURSECHECKOUT":
                return NURSECHECKOUT;
            case "HL7CHECKOUT":
                return HL7CHECKOUT;
            case "FORCECHECKOUT":
                return FORCECHECKOUT;
            case "SERVING":
                return SERVING;
            default:
                return FLOTING;
        }
    }

    private ApptStatus() {
    }

    private ApptStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
