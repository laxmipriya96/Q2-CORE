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

public class NotificationMsgRequest {
    
    private String identifier;
    private NotificationPayload payload;

    public NotificationMsgRequest(String identifier, NotificationPayload notificationPayload) {
        this.identifier = identifier;
        this.payload = notificationPayload;
    }

    public NotificationMsgRequest() {
    }
    
    

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public NotificationPayload getPayload() {
        return payload;
    }

    public void setPayload(NotificationPayload payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "NotificationMsgRequest{" + "identifier=" + identifier + ", payload=" + payload + '}';
    }

}
