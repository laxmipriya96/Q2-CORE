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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_visit_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblVisitDetail.findAll", query = "SELECT t FROM TblVisitDetail t")
    , @NamedQuery(name = "TblVisitDetail.findById", query = "SELECT t FROM TblVisitDetail t WHERE t.id = :id")
    , @NamedQuery(name = "TblVisitDetail.findByLocationId", query = "SELECT t FROM TblVisitDetail t WHERE t.locationId = :locationId")
    , @NamedQuery(name = "TblVisitDetail.findByBranchId", query = "SELECT t FROM TblVisitDetail t WHERE t.branchId = :branchId")
    , @NamedQuery(name = "TblVisitDetail.findByServiceId", query = "SELECT t FROM TblVisitDetail t WHERE t.serviceId = :serviceId")
    , @NamedQuery(name = "TblVisitDetail.findByStartTime", query = "SELECT t FROM TblVisitDetail t WHERE t.startTime = :startTime")
    , @NamedQuery(name = "TblVisitDetail.findByEndTime", query = "SELECT t FROM TblVisitDetail t WHERE t.endTime = :endTime")
    , @NamedQuery(name = "TblVisitDetail.findByRoomNo", query = "SELECT t FROM TblVisitDetail t WHERE t.roomNo = :roomNo")
    , @NamedQuery(name = "TblVisitDetail.findByServedBy", query = "SELECT t FROM TblVisitDetail t WHERE t.servedBy = :servedBy")
    , @NamedQuery(name = "TblVisitDetail.findByDrId", query = "SELECT t FROM TblVisitDetail t WHERE t.drId = :drId")})
public class TblVisitDetail implements Serializable {

    @Column(name = "trans_id")
    private String transId;
    @Column(name = "status")
    private String status;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long visitDetailsId;

    @Column(name = "location_id")
    private Long locationId;
    @Column(name = "branch_id")
    private Long branchId;
    @Column(name = "service_id")
    private Long serviceId;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Column(name = "room_no")
    private Long roomNo;
    @Column(name = "served_by")
    private Long servedBy;
    @Column(name = "dr_id")
    private Long drId;

    public TblVisitDetail() {
    
    }

    public Long getVisitDetailsId() {
        return visitDetailsId;
    }

    public void setVisitDetailsId(Long visitDetailsId) {
        this.visitDetailsId = visitDetailsId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(Long roomNo) {
        this.roomNo = roomNo;
    }

    public Long getServedBy() {
        return servedBy;
    }

    public void setServedBy(Long servedBy) {
        this.servedBy = servedBy;
    }

    public Long getDrId() {
        return drId;
    }

    public void setDrId(Long drId) {
        this.drId = drId;
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

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TblVisitDetail{" + "transId=" + transId + ", status=" + status + ", visitDetailsId=" + visitDetailsId + ", locationId=" + locationId + ", branchId=" + branchId + ", serviceId=" + serviceId + ", startTime=" + startTime + ", endTime=" + endTime + ", roomNo=" + roomNo + ", servedBy=" + servedBy + ", drId=" + drId + '}';
    }
    
    
}
