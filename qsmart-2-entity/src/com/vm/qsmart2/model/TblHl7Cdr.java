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
@Table(name = "tbl_hl7_cdr")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblHl7Cdr.findAll", query = "SELECT t FROM TblHl7Cdr t")
    , @NamedQuery(name = "TblHl7Cdr.findById", query = "SELECT t FROM TblHl7Cdr t WHERE t.id = :id")
    , @NamedQuery(name = "TblHl7Cdr.findByMsgType", query = "SELECT t FROM TblHl7Cdr t WHERE t.msgType = :msgType")
    , @NamedQuery(name = "TblHl7Cdr.findByMrnNo", query = "SELECT t FROM TblHl7Cdr t WHERE t.mrnNo = :mrnNo")
    , @NamedQuery(name = "TblHl7Cdr.findByNationalId", query = "SELECT t FROM TblHl7Cdr t WHERE t.nationalId = :nationalId")
    , @NamedQuery(name = "TblHl7Cdr.findByDob", query = "SELECT t FROM TblHl7Cdr t WHERE t.dob = :dob")
    , @NamedQuery(name = "TblHl7Cdr.findByGender", query = "SELECT t FROM TblHl7Cdr t WHERE t.gender = :gender")
    , @NamedQuery(name = "TblHl7Cdr.findByMobileNo", query = "SELECT t FROM TblHl7Cdr t WHERE t.mobileNo = :mobileNo")
    , @NamedQuery(name = "TblHl7Cdr.findByApptStartTime", query = "SELECT t FROM TblHl7Cdr t WHERE t.apptStartTime = :apptStartTime")
    , @NamedQuery(name = "TblHl7Cdr.findByApptType", query = "SELECT t FROM TblHl7Cdr t WHERE t.apptType = :apptType")
    , @NamedQuery(name = "TblHl7Cdr.findByApptCode", query = "SELECT t FROM TblHl7Cdr t WHERE t.apptCode = :apptCode")
    , @NamedQuery(name = "TblHl7Cdr.findByApptEndTime", query = "SELECT t FROM TblHl7Cdr t WHERE t.apptEndTime = :apptEndTime")
    , @NamedQuery(name = "TblHl7Cdr.findByResourceCode", query = "SELECT t FROM TblHl7Cdr t WHERE t.resourceCode = :resourceCode")
    , @NamedQuery(name = "TblHl7Cdr.findByDrFirstName", query = "SELECT t FROM TblHl7Cdr t WHERE t.drFirstName = :drFirstName")
    , @NamedQuery(name = "TblHl7Cdr.findByDrMiddleName", query = "SELECT t FROM TblHl7Cdr t WHERE t.drMiddleName = :drMiddleName")
    , @NamedQuery(name = "TblHl7Cdr.findByDrLastName", query = "SELECT t FROM TblHl7Cdr t WHERE t.drLastName = :drLastName")
    , @NamedQuery(name = "TblHl7Cdr.findByRemarks", query = "SELECT t FROM TblHl7Cdr t WHERE t.remarks = :remarks")
    , @NamedQuery(name = "TblHl7Cdr.findByCreatedOn", query = "SELECT t FROM TblHl7Cdr t WHERE t.createdOn = :createdOn")
    , @NamedQuery(name = "TblHl7Cdr.findByReceivedDate", query = "SELECT t FROM TblHl7Cdr t WHERE t.receivedDate = :receivedDate")})
public class TblHl7Cdr implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hl7CdrId;
    @Column(name = "msg_type")
    private String msgType;
    @Column(name = "mrn_no")
    private String mrnNo;
    @Column(name = "location_code")
    private String locationCode;
    @Column(name = "status")
    private Integer status;
    @Column(name = "national_id")
    private String nationalId;
    @Column(name = "dob")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob;
    @Column(name = "gender")
    private String gender;
    @Column(name = "mobile_no")
    private String mobileNo;
    @Column(name = "appt_start_time")
    private String apptStartTime;
    @Column(name = "appt_type")
    private String apptType;
    @Column(name = "appt_code")
    private String apptCode;
    @Column(name = "appt_end_time")
    private String apptEndTime;
    @Column(name = "encounter_id")
    private String encounterId ;
    @Column(name = "service_location")
    private String serviceLocation ;
    @Column(name = "resource_code")
    private String resourceCode;
    @Column(name = "dr_first_name")
    private String drFirstName;
    @Column(name = "dr_middle_name")
    private String drMiddleName;
    @Column(name = "dr_last_name")
    private String drLastName;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "received_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedDate;
    @Column(name = "appt_status")
    private String apptStatus;
    @Column(name = "clerk_id")
    private Long clerkId;
    @Column(name = "reason_to_visit")
    private String reasonToVist;
    @Column(name = "priority_flag")
    private String priorityFlag;
    
    public TblHl7Cdr() {
    }

    public String getPriorityFlag() {
        return priorityFlag;
    }

    public void setPriorityFlag(String priorityFlag) {
        this.priorityFlag = priorityFlag;
    }
    
    public String getApptStatus() {
        return apptStatus;
    }

    public void setApptStatus(String apptStatus) {
        this.apptStatus = apptStatus;
    }
    
    public TblHl7Cdr(Long id) {
        this.hl7CdrId = id;
    }

    public Long getClerkId() {
        return clerkId;
    }

    public void setClerkId(Long clerkId) {
        this.clerkId = clerkId;
    }

    public String getReasonToVist() {
        return reasonToVist;
    }

    public void setReasonToVist(String reasonToVist) {
        this.reasonToVist = reasonToVist;
    }
    

    public Long getHl7CdrId() {
        return hl7CdrId;
    }

    public void setHl7CdrId(Long hl7CdrId) {
        this.hl7CdrId = hl7CdrId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMrnNo() {
        return mrnNo;
    }

    public void setMrnNo(String mrnNo) {
        this.mrnNo = mrnNo;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getApptStartTime() {
        return apptStartTime;
    }

    public void setApptStartTime(String apptStartTime) {
        this.apptStartTime = apptStartTime;
    }

    public String getApptEndTime() {
        return apptEndTime;
    }

    public void setApptEndTime(String apptEndTime) {
        this.apptEndTime = apptEndTime;
    }
    
    

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getApptType() {
        return apptType;
    }

    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

    public String getApptCode() {
        return apptCode;
    }

    public void setApptCode(String apptCode) {
        this.apptCode = apptCode;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getDrFirstName() {
        return drFirstName;
    }

    public void setDrFirstName(String drFirstName) {
        this.drFirstName = drFirstName;
    }

    public String getDrMiddleName() {
        return drMiddleName;
    }

    public void setDrMiddleName(String drMiddleName) {
        this.drMiddleName = drMiddleName;
    }

    public String getDrLastName() {
        return drLastName;
    }

    public void setDrLastName(String drLastName) {
        this.drLastName = drLastName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(String encounterId) {
        this.encounterId = encounterId;
    }

    public String getServiceLocation() {
        return serviceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblHl7Cdr[ id=" + hl7CdrId + " ]";
    }
    
}
