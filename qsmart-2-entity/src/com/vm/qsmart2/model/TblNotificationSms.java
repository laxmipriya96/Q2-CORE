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
@Table(name = "tbl_notification_sms")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "TblNotificationSms.findAll", query = "SELECT t FROM TblNotificationSms t")
//    , @NamedQuery(name = "TblNotificationSms.findById", query = "SELECT t FROM TblNotificationSms t WHERE t.id = :id")
//    , @NamedQuery(name = "TblNotificationSms.findByTemplateName", query = "SELECT t FROM TblNotificationSms t WHERE t.templateName = :templateName")
//    , @NamedQuery(name = "TblNotificationSms.findByEnterpriseId", query = "SELECT t FROM TblNotificationSms t WHERE t.enterpriseId = :enterpriseId")
//    , @NamedQuery(name = "TblNotificationSms.findByMobileNo", query = "SELECT t FROM TblNotificationSms t WHERE t.mobileNo = :mobileNo")
//    , @NamedQuery(name = "TblNotificationSms.findBySmsTxt", query = "SELECT t FROM TblNotificationSms t WHERE t.smsTxt = :smsTxt")
//    , @NamedQuery(name = "TblNotificationSms.findByTriggerId", query = "SELECT t FROM TblNotificationSms t WHERE t.triggerId = :triggerId")
//    , @NamedQuery(name = "TblNotificationSms.findByCreatedOn", query = "SELECT t FROM TblNotificationSms t WHERE t.createdOn = :createdOn")
//    , @NamedQuery(name = "TblNotificationSms.findByCreatedBy", query = "SELECT t FROM TblNotificationSms t WHERE t.createdBy = :createdBy")
//    , @NamedQuery(name = "TblNotificationSms.findByUpdatedOn", query = "SELECT t FROM TblNotificationSms t WHERE t.updatedOn = :updatedOn")
//    , @NamedQuery(name = "TblNotificationSms.findByUpdatedBy", query = "SELECT t FROM TblNotificationSms t WHERE t.updatedBy = :updatedBy")})
public class TblNotificationSms implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long notificationSmsId;
    @Column(name = "template_name")
    private String templateName;
    @Column(name = "enterprise_id")
    private Long enterpriseId;
    @Column(name = "mobile_no")
    private String mobileNo;
    @Column(name = "sms_txt")
    private String smsTxt;
    @Column(name = "trigger_id")
    private Long triggerId;
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

    public TblNotificationSms() {
    }

    public TblNotificationSms(Long id) {
        this.notificationSmsId = id;
    }

   

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getSmsTxt() {
        return smsTxt;
    }

    public void setSmsTxt(String smsTxt) {
        this.smsTxt = smsTxt;
    }

    public Long getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(Long triggerId) {
        this.triggerId = triggerId;
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

    public Long getNotificationSmsId() {
        return notificationSmsId;
    }

    public void setNotificationSmsId(Long notificationSmsId) {
        this.notificationSmsId = notificationSmsId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
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
        hash += (notificationSmsId != null ? notificationSmsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblNotificationSms)) {
            return false;
        }
        TblNotificationSms other = (TblNotificationSms) object;
        if ((this.notificationSmsId == null && other.notificationSmsId != null) || (this.notificationSmsId != null && !this.notificationSmsId.equals(other.notificationSmsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblNotificationSms[ id=" + notificationSmsId + " ]";
    }
    
}
