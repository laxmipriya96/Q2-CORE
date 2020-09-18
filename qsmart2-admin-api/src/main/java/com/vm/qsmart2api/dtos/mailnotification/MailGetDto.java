/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.mailnotification;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class MailGetDto {
    private List<MailUpDto> mailNotifications;

    public MailGetDto() {
    }

    public MailGetDto(List<MailUpDto> mailNotifications) {
        this.mailNotifications = mailNotifications;
    }

    public List<MailUpDto> getMailNotifications() {
        return mailNotifications;
    }

    public void setMailNotifications(List<MailUpDto> mailNotifications) {
        this.mailNotifications = mailNotifications;
    }

    @Override
    public String toString() {
        return "MailGetDto{" + "mailNotifications=" + mailNotifications + '}';
    }
    
}
