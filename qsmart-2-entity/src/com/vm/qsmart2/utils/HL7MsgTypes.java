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
public enum HL7MsgTypes {

    S12("SIU-S12"), S13("SIU-S13"), S15("SIU-S15"), Z01("SIU-ZO1"), A03("ADT-A03"), A05("ADT-A05");

    private String hl7FileType;

    private HL7MsgTypes(String hl7FileType) {
        this.hl7FileType = hl7FileType;
    }

    public String getHl7FileType() {
        return hl7FileType;
    }

    public void setHl7FileType(String hl7FileType) {
        this.hl7FileType = hl7FileType;
    }

    public static HL7MsgTypes getInstce(String value) {
        switch (value) {
            case "SIU-S12":
                return S12;
            case "SIU-S13":
                return S13;
            case "SIU-S15":
                return S15;
            case "SIU-Z01":
                return Z01;
            case "ADT-A05":
                return A05;
            case "ADT-A03":
                return A03;
            default:
                return S15;
        }
    }

}
