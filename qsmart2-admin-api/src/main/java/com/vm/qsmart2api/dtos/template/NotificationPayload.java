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
public class NotificationPayload {
    
    private String text;

    public NotificationPayload() {
    }

    public NotificationPayload(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "NotificationPayload{" + "text=" + text + '}';
    }
    
}
