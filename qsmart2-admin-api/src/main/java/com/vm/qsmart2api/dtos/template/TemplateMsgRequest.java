/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.template;

/**
 *
 * @author Phani
 */
public class TemplateMsgRequest {

    private String identifier;
    private TemplateMsgPayload payload;

    public TemplateMsgRequest() {
    }

    public TemplateMsgRequest(String identifier, TemplateMsgPayload payload) {
        this.identifier = identifier;
        this.payload = payload;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public TemplateMsgPayload getPayload() {
        return payload;
    }

    public void setPayload(TemplateMsgPayload payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "TemplateMsgRequest{" + "identifier=" + identifier + ", payload=" + payload + '}';
    }
    
}
