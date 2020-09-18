/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Phani
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hl7MsgTrigger {
    
    public long hl7CdrId;
    public long patientId;
    public HL7MsgTypes hl7MsgType;
    public String tableName;

    public Hl7MsgTrigger() {
    }

    public Hl7MsgTrigger(long hl7CdrId, long patientId, HL7MsgTypes hl7MsgType, String tableName) {
        this.hl7CdrId = hl7CdrId;
        this.patientId = patientId;
        this.hl7MsgType = hl7MsgType;
        this.tableName = tableName;
    }
    
    

    public long getHl7CdrId() {
        return hl7CdrId;
    }

    public void setHl7CdrId(long hl7CdrId) {
        this.hl7CdrId = hl7CdrId;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public HL7MsgTypes getHl7MsgType() {
        return hl7MsgType;
    }

    public void setHl7MsgType(HL7MsgTypes hl7MsgType) {
        this.hl7MsgType = hl7MsgType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "Hl7FileTrigger{" + "hl7CdrId=" + hl7CdrId + ", patientId=" + patientId + ", hl7MsgType=" + hl7MsgType + ", tableName=" + tableName + '}';
    }
    
}
