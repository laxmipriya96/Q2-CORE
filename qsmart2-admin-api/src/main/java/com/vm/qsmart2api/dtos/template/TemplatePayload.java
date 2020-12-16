/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Phani
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplatePayload {
    
    private String type;
    private TemplateNotification notification;

    public TemplatePayload() {
    }

    public TemplatePayload(String type, TemplateNotification notification) {
        this.type = type;
        this.notification = notification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TemplateNotification getNotification() {
        return notification;
    }

    public void setNotification(TemplateNotification notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        return "TemplatePayload{" + "type=" + type + ", notification=" + notification + '}';
    }
    
}
