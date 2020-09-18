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
@Table(name = "tbl_patient")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblPatient.findAll", query = "SELECT t FROM TblPatient t")
    , @NamedQuery(name = "TblPatient.findById", query = "SELECT t FROM TblPatient t WHERE t.id = :id")
    , @NamedQuery(name = "TblPatient.findByMrnNo", query = "SELECT t FROM TblPatient t WHERE t.mrnNo = :mrnNo")
    , @NamedQuery(name = "TblPatient.findByNationalId", query = "SELECT t FROM TblPatient t WHERE t.nationalId = :nationalId")
    , @NamedQuery(name = "TblPatient.findByFirstName", query = "SELECT t FROM TblPatient t WHERE t.firstName = :firstName")
    , @NamedQuery(name = "TblPatient.findByMiddleName", query = "SELECT t FROM TblPatient t WHERE t.middleName = :middleName")
    , @NamedQuery(name = "TblPatient.findByLastName", query = "SELECT t FROM TblPatient t WHERE t.lastName = :lastName")
    , @NamedQuery(name = "TblPatient.findByDob", query = "SELECT t FROM TblPatient t WHERE t.dob = :dob")
    , @NamedQuery(name = "TblPatient.findByGender", query = "SELECT t FROM TblPatient t WHERE t.gender = :gender")
    , @NamedQuery(name = "TblPatient.findByCreatedOn", query = "SELECT t FROM TblPatient t WHERE t.createdOn = :createdOn")
    , @NamedQuery(name = "TblPatient.findByUpdatedOn", query = "SELECT t FROM TblPatient t WHERE t.updatedOn = :updatedOn")})
public class TblPatient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long patientId;
    @Column(name = "mrn_no")
    private String mrnNo;
    @Column(name = "national_id")
    private String nationalId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Column(name = "gender")
    private String gender;
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;
    @Column(name = "priority")
    private Short priority;
    @Column(name = "mobile_no")
    private String mobileNo;
    @Column(name = "mail_address")
    private String mailAddress;

    public TblPatient() {
    }

    public TblPatient(Long id) {
        this.patientId = id;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }
    
    public Short getPriority() {
        return priority;
    }

    public void setPriority(Short priority) {
        this.priority = priority;
    }
    
    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
    public String toString() {
        return "com.vm.qsmart2.model.TblPatient[ id=" + patientId + " ]";
    }
    
}
