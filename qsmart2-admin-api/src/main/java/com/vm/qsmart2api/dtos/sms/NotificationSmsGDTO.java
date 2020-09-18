/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.sms;

import java.util.List;

/**
 *
 * @author Ashok
 */
public class NotificationSmsGDTO {
    
    private List<NotificationSmsUDTO> smsNotification;

    public NotificationSmsGDTO(List<NotificationSmsUDTO> notificationSmsUDTO) {
       this.smsNotification = notificationSmsUDTO;
    }

    public NotificationSmsGDTO() {
   
    }

    /**
     * @return the smsNotification
     */
    public List<NotificationSmsUDTO> getSmsNotification() {
        return smsNotification;
    }

    /**
     * @param smsNotification the smsNotification to set
     */
    public void setSmsNotification(List<NotificationSmsUDTO> smsNotification) {
        this.smsNotification = smsNotification;
    }
    
}
