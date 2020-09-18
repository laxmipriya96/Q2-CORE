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
@Table(name = "tbl_visit_summary")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblVisitSummary.findAll", query = "SELECT t FROM TblVisitSummary t")
    , @NamedQuery(name = "TblVisitSummary.findById", query = "SELECT t FROM TblVisitSummary t WHERE t.id = :id")
    , @NamedQuery(name = "TblVisitSummary.findByStartTime", query = "SELECT t FROM TblVisitSummary t WHERE t.startTime = :startTime")
    , @NamedQuery(name = "TblVisitSummary.findByEndTime", query = "SELECT t FROM TblVisitSummary t WHERE t.endTime = :endTime")
    , @NamedQuery(name = "TblVisitSummary.findByCheckInTime", query = "SELECT t FROM TblVisitSummary t WHERE t.checkInTime = :checkInTime")
    , @NamedQuery(name = "TblVisitSummary.findByFirstCallTime", query = "SELECT t FROM TblVisitSummary t WHERE t.firstCallTime = :firstCallTime")
    , @NamedQuery(name = "TblVisitSummary.findByTotalWaitTime", query = "SELECT t FROM TblVisitSummary t WHERE t.totalWaitTime = :totalWaitTime")
    , @NamedQuery(name = "TblVisitSummary.findByTotalCareTime", query = "SELECT t FROM TblVisitSummary t WHERE t.totalCareTime = :totalCareTime")
    , @NamedQuery(name = "TblVisitSummary.findByPatientId", query = "SELECT t FROM TblVisitSummary t WHERE t.patientId = :patientId")
    , @NamedQuery(name = "TblVisitSummary.findByLocationId", query = "SELECT t FROM TblVisitSummary t WHERE t.locationId = :locationId")
    , @NamedQuery(name = "TblVisitSummary.findByBranchId", query = "SELECT t FROM TblVisitSummary t WHERE t.branchId = :branchId")
    , @NamedQuery(name = "TblVisitSummary.findByCheckOutTime", query = "SELECT t FROM TblVisitSummary t WHERE t.checkOutTime = :checkOutTime")})
public class TblVisitSummary implements Serializable {

    @Column(name = "trans_id", updatable = false)
    private String transId;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long visitSummaryId;
    
    @Column(name = "start_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "end_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Column(name = "check_in_time", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkInTime;
    @Column(name = "first_call_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstCallTime;
    @Column(name = "total_wait_time")
    private Integer totalWaitTime;
    @Column(name = "total_care_time")
    private Integer totalCareTime;
    @Column(name = "patient_id")
    private Long patientId;
    @Column(name = "location_id")
    private Long locationId;
    @Column(name = "branch_id")
    private Long branchId;
    @Column(name = "check_out_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOutTime;

    public TblVisitSummary() {
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

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Date getFirstCallTime() {
        return firstCallTime;
    }

    public void setFirstCallTime(Date firstCallTime) {
        this.firstCallTime = firstCallTime;
    }

    public Integer getTotalWaitTime() {
        return totalWaitTime;
    }

    public void setTotalWaitTime(Integer totalWaitTime) {
        this.totalWaitTime = totalWaitTime;
    }

    public Integer getTotalCareTime() {
        return totalCareTime;
    }

    public void setTotalCareTime(Integer totalCareTime) {
        this.totalCareTime = totalCareTime;
    }

    public Date getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(Date checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Long getVisitSummaryId() {
        return visitSummaryId;
    }

    public void setVisitSummaryId(Long visitSummaryId) {
        this.visitSummaryId = visitSummaryId;
    }


    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
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


    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    @Override
    public String toString() {
        return "TblVisitSummary{" + "transId=" + transId + ", visitSummaryId=" + visitSummaryId + ", startTime=" + startTime + ", endTime=" + endTime + ", checkInTime=" + checkInTime + ", firstCallTime=" + firstCallTime + ", totalWaitTime=" + totalWaitTime + ", totalCareTime=" + totalCareTime + ", patientId=" + patientId + ", locationId=" + locationId + ", branchId=" + branchId + ", checkOutTime=" + checkOutTime + '}';
    }

   
    
}
