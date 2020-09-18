/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_service")
@XmlRootElement
public class TblService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long serviceId;
    @Basic(optional = false)
    @Column(name = "service_name_en")
    private String serviceNameEn;
    @Basic(optional = false)
    @Column(name = "service_name_ar")
    private String serviceNameAr;
    @Basic(optional = false)
    @Column(name = "token_prefix")
    private String tokenPrefix;
    @Basic(optional = false)
    @Column(name = "start_seq")
    private int startSeq;
    @Basic(optional = false)
    @Column(name = "end_seq")
    private int endSeq;
    @Basic(optional = false)
    @Column(name = "branch_id")
    private Long branchId;
    @Basic(optional = false)
    @Column(name = "wait_time_avg")
    private int waitTimeAvg;
    @Basic(optional = false)
    @Column(name = "min_checkin_time")
    private int minCheckinTime;
    @Basic(optional = false)
    @Column(name = "max_checkin_time")
    private int maxCheckinTime;
    @Basic(optional = false)

    @Column(name = "created_by", updatable = false)
    private Long createdBy;
    @Basic(optional = false)

    @Column(name = "created_on", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "updated_by")
    private Long updatedBy;
    @Basic(optional = false)
    @Column(name = "updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;
    @Basic(optional = false)
    @Column(name = "is_default")
    private short isDefault;

    @ManyToMany
    @JoinTable(name = "tbl_service_appt_type", joinColumns = {
        @JoinColumn(name = "service_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "appt_type_id", referencedColumnName = "id")})
    private Set<TblApptType> apptTypes;

    @ManyToMany
    @JoinTable(name = "tbl_service_display", joinColumns = {
        @JoinColumn(name = "service_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "display_id", referencedColumnName = "id")})
    private Set<TblDisplay> displayBoards;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "service")
    private Set<TblServiceBooked> serviceBookings;

//    @Column(name = "is_vital")
//    private Short isVital;

    public TblService() {
    }

    public Set<TblServiceBooked> getServiceBookings() {
        return serviceBookings;
    }

    public void setServiceBookings(Set<TblServiceBooked> serviceBookings) {
        this.serviceBookings = serviceBookings;
    }

    public Set<TblDisplay> getDisplayBoards() {
        return displayBoards;
    }

    public void setDisplayBoards(Set<TblDisplay> displayBoards) {
        this.displayBoards = displayBoards;
    }

//    public Short getIsVital() {
//        return isVital;
//    }
//
//    public void setIsVital(Short isVital) {
//        this.isVital = isVital;
//    }

    public Set<TblApptType> getApptTypes() {
        return apptTypes;
    }

    public void setApptTypes(Set<TblApptType> apptTypes) {
        this.apptTypes = apptTypes;
    }

    public TblService(Long serviceId) {
        this.serviceId = serviceId;
    }

    public TblService(Long serviceId, String serviceNameEn, String serviceNameAr, String tokenPrefix, int startSeq, int endSeq, Long branchId, int waitTimeAvg, int minCheckinTime, int maxCheckinTime, Long createdBy, Date createdOn, Long updatedBy, Date updatedOn) {
        this.serviceId = serviceId;
        this.serviceNameEn = serviceNameEn;
        this.serviceNameAr = serviceNameAr;
        this.tokenPrefix = tokenPrefix;
        this.startSeq = startSeq;
        this.endSeq = endSeq;
        this.branchId = branchId;
        this.waitTimeAvg = waitTimeAvg;
        this.minCheckinTime = minCheckinTime;
        this.maxCheckinTime = maxCheckinTime;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @return the isDefault
     */
    public short getIsDefault() {
        return isDefault;
    }

    /**
     * @param isDefault the isDefault to set
     */
    public void setIsDefault(short isDefault) {
        this.isDefault = isDefault;
    }

    public String getServiceNameEn() {
        return serviceNameEn;
    }

    public void setServiceNameEn(String serviceNameEn) {
        this.serviceNameEn = serviceNameEn;
    }

    public String getServiceNameAr() {
        return serviceNameAr;
    }

    public void setServiceNameAr(String serviceNameAr) {
        this.serviceNameAr = serviceNameAr;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public int getStartSeq() {
        return startSeq;
    }

    public void setStartSeq(int startSeq) {
        this.startSeq = startSeq;
    }

    public int getEndSeq() {
        return endSeq;
    }

    public void setEndSeq(int endSeq) {
        this.endSeq = endSeq;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public int getWaitTimeAvg() {
        return waitTimeAvg;
    }

    public void setWaitTimeAvg(int waitTimeAvg) {
        this.waitTimeAvg = waitTimeAvg;
    }

    public int getMinCheckinTime() {
        return minCheckinTime;
    }

    public void setMinCheckinTime(int minCheckinTime) {
        this.minCheckinTime = minCheckinTime;
    }

    public int getMaxCheckinTime() {
        return maxCheckinTime;
    }

    public void setMaxCheckinTime(int maxCheckinTime) {
        this.maxCheckinTime = maxCheckinTime;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
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
        hash += (serviceId != null ? serviceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblService)) {
            return false;
        }
        TblService other = (TblService) object;
        if ((this.serviceId == null && other.serviceId != null) || (this.serviceId != null && !this.serviceId.equals(other.serviceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblService[ id=" + serviceId + " ]";
    }

}
