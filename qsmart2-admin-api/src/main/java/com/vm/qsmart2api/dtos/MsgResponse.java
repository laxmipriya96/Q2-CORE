/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Phani
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MsgResponse {
     private String id;
     private String messenger;
     private int message_amount;
     private boolean split;
     private String status;
     private String error;
     private String hint;
     private int submitStatus;

    public MsgResponse() {
    }

    public MsgResponse(String id, String messenger, int message_amount, boolean split, String status, String error, String hint, int submitStatus) {
        this.id = id;
        this.messenger = messenger;
        this.message_amount = message_amount;
        this.split = split;
        this.status = status;
        this.error = error;
        this.hint = hint;
        this.submitStatus = submitStatus;
    }
     
     

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(int submitStatus) {
        this.submitStatus = submitStatus;
    }
     
     

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }

    public int getMessage_amount() {
        return message_amount;
    }

    public void setMessage_amount(int message_amount) {
        this.message_amount = message_amount;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MsgResponse{" + "id=" + id + ", messenger=" + messenger + ", message_amount=" + message_amount + ", split=" + split + ", status=" + status + ", error=" + error + ", hint=" + hint + ", submitStatus=" + submitStatus + '}';
    }
    
}
