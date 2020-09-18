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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_service_booked")
@XmlRootElement
public class TblServiceBooked implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long serviceBookedId;
    
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @Column(name = "encounter_id")
    private String encounterId;
    @Column(name = "reason_to_visit")
    private String reasonToVisit;
    @Column(name = "dr_id")
    private Long drId;
//    @Column(name = "status")
//    private Integer status;

    @JoinColumn(name = "trans_id", referencedColumnName = "trans_id")
    @ManyToOne(optional = false)
    private TblAppointment appointment;
    
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TblService service;
    
    @Column(name = "service_status")
    private String serviceStatus;

    public TblServiceBooked() {
    }

    public TblServiceBooked(Long serviceBookedId) {
        this.serviceBookedId = serviceBookedId;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
    
    public Long getServiceBookedId() {
        return serviceBookedId;
    }

    public void setServiceBookedId(Long serviceBookedId) {
        this.serviceBookedId = serviceBookedId;
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

    public String getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(String encounterId) {
        this.encounterId = encounterId;
    }

    public String getReasonToVisit() {
        return reasonToVisit;
    }

    public void setReasonToVisit(String reasonToVisit) {
        this.reasonToVisit = reasonToVisit;
    }

    public Long getDrId() {
        return drId;
    }

    public void setDrId(Long drId) {
        this.drId = drId;
    }

//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (serviceBookedId != null ? serviceBookedId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblServiceBooked)) {
            return false;
        }
        TblServiceBooked other = (TblServiceBooked) object;
        if ((this.serviceBookedId == null && other.serviceBookedId != null) || (this.serviceBookedId != null && !this.serviceBookedId.equals(other.serviceBookedId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblServiceBooked[ id=" + serviceBookedId + " ]";
    }

    /**
     * @return the appointment
     */
    public TblAppointment getAppointment() {
        return appointment;
    }

    /**
     * @param appointment the appointment to set
     */
    public void setAppointment(TblAppointment appointment) {
        this.appointment = appointment;
    }

    /**
     * @return the service
     */
    public TblService getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(TblService service) {
        this.service = service;
    }

}
