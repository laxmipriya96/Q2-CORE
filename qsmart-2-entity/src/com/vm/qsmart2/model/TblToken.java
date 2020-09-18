/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_token")
@XmlRootElement
public class TblToken implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long tokenId;
    @Column(name = "token_no")
    private String tokenNo;
    @Column(name = "status")
    private String status;
    @Column(name = "room_no")
    private Long roomNo;
    @Column(name = "start_time", updatable = false)
    private Date startTime;
    @Column(name = "end_time", updatable = false)
    private Date endTime;
    @Column(name = "first_call")
    private Integer firstCall;
    @Column(name = "no_show")
    private Integer noShow;
    @Column(name = "served_by")
    private Long servedBy;
    @Column(name = "created_on", updatable = false)
    private Date createdOn;
    @Column(name = "updated_on")
    private Date updatedOn;
    @Column(name = "priority")
    private Short priority;
    
//    @Column(name = "trans_id")
//    private String tranId;
    
//    @OneToOne(cascade = CascadeType.MERGE)
//    @JoinColumn(name = "appt_id", referencedColumnName = "id")
//    private TblAppointment appaitment;
//    
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "trans_id", referencedColumnName = "trans_id")
    private TblAppointment appaitment;

    public Short getPriority() {
        return priority;
    }

    public void setPriority(Short priority) {
        this.priority = priority;
    }
    
    public TblToken() {
    }

    public TblToken(Long tokenId) {
        this.tokenId = tokenId;
    }

//    public String getTranId() {
//        return tranId;
//    }
//
//    public void setTranId(String tranId) {
//        this.tranId = tranId;
//    }
    
    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(Long roomNo) {
        this.roomNo = roomNo;
    }


    public Integer getFirstCall() {
        return firstCall;
    }

    public void setFirstCall(Integer firstCall) {
        this.firstCall = firstCall;
    }

    public Integer getNoShow() {
        return noShow;
    }

    public void setNoShow(Integer noShow) {
        this.noShow = noShow;
    }

    public Long getServedBy() {
        return servedBy;
    }

    public void setServedBy(Long servedBy) {
        this.servedBy = servedBy;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tokenId != null ? tokenId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblToken)) {
            return false;
        }
        TblToken other = (TblToken) object;
        if ((this.tokenId == null && other.tokenId != null) || (this.tokenId != null && !this.tokenId.equals(other.tokenId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblToken[ id=" + tokenId + " ]";
    }

    public TblAppointment getAppaitment() {
        return appaitment;
    }

    public void setAppaitment(TblAppointment appaitment) {
        this.appaitment = appaitment;
    }

}
