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
@Table(name = "tbl_mail_queue")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "TblMailQueue.findAll", query = "SELECT t FROM TblMailQueue t")
//    , @NamedQuery(name = "TblMailQueue.findById", query = "SELECT t FROM TblMailQueue t WHERE t.id = :id")
//    , @NamedQuery(name = "TblMailQueue.findByMailAccount", query = "SELECT t FROM TblMailQueue t WHERE t.mailAccount = :mailAccount")
//    , @NamedQuery(name = "TblMailQueue.findByEnterpriseId", query = "SELECT t FROM TblMailQueue t WHERE t.enterpriseId = :enterpriseId")
//    , @NamedQuery(name = "TblMailQueue.findByLocationId", query = "SELECT t FROM TblMailQueue t WHERE t.locationId = :locationId")
//    , @NamedQuery(name = "TblMailQueue.findByTriggerId", query = "SELECT t FROM TblMailQueue t WHERE t.triggerId = :triggerId")
//    , @NamedQuery(name = "TblMailQueue.findByToMail", query = "SELECT t FROM TblMailQueue t WHERE t.toMail = :toMail")
//    , @NamedQuery(name = "TblMailQueue.findByMailSubject", query = "SELECT t FROM TblMailQueue t WHERE t.mailSubject = :mailSubject")
//    , @NamedQuery(name = "TblMailQueue.findByMailBody", query = "SELECT t FROM TblMailQueue t WHERE t.mailBody = :mailBody")
//    , @NamedQuery(name = "TblMailQueue.findByStatus", query = "SELECT t FROM TblMailQueue t WHERE t.status = :status")
//    , @NamedQuery(name = "TblMailQueue.findByCreatedOn", query = "SELECT t FROM TblMailQueue t WHERE t.createdOn = :createdOn")
//    , @NamedQuery(name = "TblMailQueue.findBySubmitDate", query = "SELECT t FROM TblMailQueue t WHERE t.submitDate = :submitDate")
//    , @NamedQuery(name = "TblMailQueue.findByErrCode", query = "SELECT t FROM TblMailQueue t WHERE t.errCode = :errCode")
//    , @NamedQuery(name = "TblMailQueue.findByErrText", query = "SELECT t FROM TblMailQueue t WHERE t.errText = :errText")})
public class TblMailQueue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long mailQueueId;
    @Column(name = "mail_account")
    private Long mailAccount;
    @Column(name = "enterprise_id")
    private Long enterpriseId;
    @Column(name = "location_id")
    private Long locationId;
    @Column(name = "trigger_id")
    private Long triggerId;
    @Column(name = "to_mail")
    private String toMail;
    @Column(name = "mail_subject")
    private String mailSubject;
    @Column(name = "mail_body")
    private String mailBody;
    @Column(name = "status")
    private Integer status;
    @Column(name = "created_on", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "submit_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submitDate;
    @Column(name = "err_code")
    private Integer errCode;
    @Column(name = "err_text")
    private String errText;

    public TblMailQueue() {
    }

    public Long getMailAccount() {
        return mailAccount;
    }

    public void setMailAccount(Long mailAccount) {
        this.mailAccount = mailAccount;
    }

    public Long getMailQueueId() {
        return mailQueueId;
    }

    public void setMailQueueId(Long mailQueueId) {
        this.mailQueueId = mailQueueId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(Long triggerId) {
        this.triggerId = triggerId;
    }

   
    public String getToMail() {
        return toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrText() {
        return errText;
    }

    public void setErrText(String errText) {
        this.errText = errText;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mailQueueId != null ? mailQueueId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMailQueue)) {
            return false;
        }
        TblMailQueue other = (TblMailQueue) object;
        if ((this.mailQueueId == null && other.mailQueueId != null) || (this.mailQueueId != null && !this.mailQueueId.equals(other.mailQueueId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblMailQueue[ id=" + mailQueueId + " ]";
    }
    
}
