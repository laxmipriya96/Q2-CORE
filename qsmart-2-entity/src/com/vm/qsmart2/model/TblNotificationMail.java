/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tejasri
 */
@Entity
@Table(name = "tbl_notification_mail")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "TblNotificationMail.findAll", query = "SELECT t FROM TblNotificationMail t")
//    , @NamedQuery(name = "TblNotificationMail.findById", query = "SELECT t FROM TblNotificationMail t WHERE t.id = :id")
//    , @NamedQuery(name = "TblNotificationMail.findByTemplateName", query = "SELECT t FROM TblNotificationMail t WHERE t.templateName = :templateName")
//    , @NamedQuery(name = "TblNotificationMail.findByEnterpriseId", query = "SELECT t FROM TblNotificationMail t WHERE t.enterpriseId = :enterpriseId")
//    , @NamedQuery(name = "TblNotificationMail.findByMailTo", query = "SELECT t FROM TblNotificationMail t WHERE t.mailTo = :mailTo")
//    , @NamedQuery(name = "TblNotificationMail.findByMailSubject", query = "SELECT t FROM TblNotificationMail t WHERE t.mailSubject = :mailSubject")
//    , @NamedQuery(name = "TblNotificationMail.findByMailBody", query = "SELECT t FROM TblNotificationMail t WHERE t.mailBody = :mailBody")
//    , @NamedQuery(name = "TblNotificationMail.findByTiggerId", query = "SELECT t FROM TblNotificationMail t WHERE t.tiggerId = :tiggerId")
//    , @NamedQuery(name = "TblNotificationMail.findByCreatedOn", query = "SELECT t FROM TblNotificationMail t WHERE t.createdOn = :createdOn")
//    , @NamedQuery(name = "TblNotificationMail.findByCreatedBy", query = "SELECT t FROM TblNotificationMail t WHERE t.createdBy = :createdBy")
//    , @NamedQuery(name = "TblNotificationMail.findByUpdatedOn", query = "SELECT t FROM TblNotificationMail t WHERE t.updatedOn = :updatedOn")
//    , @NamedQuery(name = "TblNotificationMail.findByUpdatedBy", query = "SELECT t FROM TblNotificationMail t WHERE t.updatedBy = :updatedBy")})
public class TblNotificationMail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long notificationMailId;
    @Column(name = "template_name")
    private String templateName;
    @Column(name = "enterprise_id")
    private Long enterpriseId;
    @Column(name = "mail_to")
    private String mailTo;
    @Column(name = "mail_subject")
    private String mailSubject;
    @Column(name = "mail_body")
    private String mailBody;
    @Column(name = "trigger_id")
    private Long tiggerId;
    @Column(name = "created_on", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "created_by", updatable = false)
    private Long createdBy;
    @Column(name = "updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;
    @Column(name = "updated_by")
    private Long updatedBy;

    public TblNotificationMail() {
    }

    public TblNotificationMail(Long id) {
        this.notificationMailId = id;
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


    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    
    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getNotificationMailId() {
        return notificationMailId;
    }

    public void setNotificationMailId(Long notificationMailId) {
        this.notificationMailId = notificationMailId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getTiggerId() {
        return tiggerId;
    }

    public void setTiggerId(Long tiggerId) {
        this.tiggerId = tiggerId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notificationMailId != null ? notificationMailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblNotificationMail)) {
            return false;
        }
        TblNotificationMail other = (TblNotificationMail) object;
        if ((this.notificationMailId == null && other.notificationMailId != null) || (this.notificationMailId != null && !this.notificationMailId.equals(other.notificationMailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblNotificationMail[ id=" + notificationMailId + " ]";
    }
    
}
