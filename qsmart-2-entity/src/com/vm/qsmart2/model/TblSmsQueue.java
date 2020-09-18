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
@Table(name = "tbl_sms_queue")
@XmlRootElement
public class TblSmsQueue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long smsQueueId;
    @Column(name = "sms_account")
    private Long smsAccount;
    @Column(name = "enterprise_id")
    private Long enterpriseId;
    @Column(name = "location_id")
    private Long locationId;
    @Column(name = "trigger_id")
    private Long triggerId;
    @Column(name = "sms_text")
    private String smsText;
    @Column(name = "mobile_no")
    private String mobileNo;
    @Column(name = "status")
    private Integer status;
    @Column(name = "submit_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submitDate;
    @Column(name = "err_code")
    private Integer errCode;
    @Column(name = "err_text")
    private String errText;

    public TblSmsQueue() {
    }

    public TblSmsQueue(Long smsQueueId) {
        this.smsQueueId = smsQueueId;
    }

    public Long getSmsQueueId() {
        return smsQueueId;
    }

    public void setSmsQueueId(Long smsQueueId) {
        this.smsQueueId = smsQueueId;
    }

    public Long getSmsAccount() {
        return smsAccount;
    }

    public void setSmsAccount(Long smsAccount) {
        this.smsAccount = smsAccount;
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

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        hash += (smsQueueId != null ? smsQueueId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSmsQueue)) {
            return false;
        }
        TblSmsQueue other = (TblSmsQueue) object;
        if ((this.smsQueueId == null && other.smsQueueId != null) || (this.smsQueueId != null && !this.smsQueueId.equals(other.smsQueueId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2api.utils.TblSmsQueue[ id=" + smsQueueId + " ]";
    }
    
}
