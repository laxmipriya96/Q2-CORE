/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.mailnotification;

/**
 *
 * @author Tejasri
 */
public class MailUpDto {
    
    private Long notificationMailId;
    private String templateName;
    private String mailTo;
    private String mailSubject;
    private String mailBody;
    private Long tiggerId;

    public MailUpDto() {
    }

    public MailUpDto(Long notificationMailId, String templateName, String mailTo, String mailSubject, String mailBody, Long tiggerId) {
        this.notificationMailId = notificationMailId;
        this.templateName = templateName;
        this.mailTo = mailTo;
        this.mailSubject = mailSubject;
        this.mailBody = mailBody;
        this.tiggerId = tiggerId;
    }

    public Long getNotificationMailId() {
        return notificationMailId;
    }

    public void setNotificationMailId(Long notificationMailId) {
        this.notificationMailId = notificationMailId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }


    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailBody() {
        return mailBody;
    }

    public void setMailBody(String mailBody) {
        this.mailBody = mailBody;
    }

    public Long getTiggerId() {
        return tiggerId;
    }

    public void setTiggerId(Long tiggerId) {
        this.tiggerId = tiggerId;
    }

    @Override
    public String toString() {
        return "MailUpDto{" + "notificationMailId=" + notificationMailId + ", templateName=" + templateName + ", mailTo=" + mailTo + ", mailSubject=" + mailSubject + ", mailBody=" + mailBody + ", tiggerId=" + tiggerId + '}';
    }

}
