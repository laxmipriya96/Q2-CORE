/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_branch")
@XmlRootElement
public class TblBranch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchId;
    @Basic(optional = false)
    @Column(name = "branch_name_en")
    private String branchNameEn;
    @Basic(optional = false)
    @Column(name = "branch_name_ar")
    private String branchNameAr;
    @Column(name = "branch_identifier")
    private String branchIdentifier;
    
    @ManyToMany
    @JoinTable(name = "tbl_branch_service_location", joinColumns = {
        @JoinColumn(name = "branch_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "service_location_id", referencedColumnName = "id")})
    private Set<TblServiceLocation> serviceLocations;

    @Column(name = "created_by",updatable = false)
    private Long createdBy;
    @Basic(optional = false)

    @Column(name = "created_on",updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
   
    @Column(name = "updated_by")
    private Long updatedBy;
    @Basic(optional = false)
   
    @Column(name = "updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;
    @JoinColumn(name = "branch_type_id", referencedColumnName = "id")
    @ManyToOne
    private TblBranchType branchType;
   
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TblLocation location;
    
    @Column(name = "is_registration")
    private Short isRegistration;
    
    @Column(name = "is_floating")
    private Short isFloating;
    
    @Column(name = "is_vital")
    private Short isVital;
    
    @ManyToMany
    @JoinTable(name = "tbl_branch_kiosk", joinColumns = {
        @JoinColumn(name = "branch_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "kiosk_id", referencedColumnName = "id")})
    private Set<TblKiosk> kiosks;

    public TblBranch() {
    }

    public TblBranch(Long branchId) {
        this.branchId = branchId;
    }

    public Short getIsRegistration() {
        return isRegistration;
    }

    public void setIsRegistration(Short isRegistration) {
        this.isRegistration = isRegistration;
    }

    public Short getIsFloating() {
        return isFloating;
    }

    public void setIsFloating(Short isFloating) {
        this.isFloating = isFloating;
    }

    public Short getIsVital() {
        return isVital;
    }

    public void setIsVital(Short isVital) {
        this.isVital = isVital;
    }
    
    public Set<TblKiosk> getKiosks() {
        return kiosks;
    }

    public void setKiosks(Set<TblKiosk> kiosks) {
        this.kiosks = kiosks;
    }


    public TblBranch(Long branchId, String branchNameEn, String branchNameAr, long createdBy, Date createdOn, Long updatedBy, Date updatedOn) {
        this.branchId = branchId;
        this.branchNameEn = branchNameEn;
        this.branchNameAr = branchNameAr;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchNameEn() {
        return branchNameEn;
    }

    public void setBranchNameEn(String branchNameEn) {
        this.branchNameEn = branchNameEn;
    }

    public String getBranchNameAr() {
        return branchNameAr;
    }

    public void setBranchNameAr(String branchNameAr) {
        this.branchNameAr = branchNameAr;
    }

    public String getBranchIdentifier() {
        return branchIdentifier;
    }

    public void setBranchIdentifier(String branchIdentifier) {
        this.branchIdentifier = branchIdentifier;
    }

    public Set<TblServiceLocation> getServiceLocations() {
        return serviceLocations;
    }

    public void setServiceLocations(Set<TblServiceLocation> serviceLocations) {
        this.serviceLocations = serviceLocations;
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

    public TblBranchType getBranchType() {
        return branchType;
    }

    public void setBranchType(TblBranchType branchType) {
        this.branchType= branchType;
    }

    public TblLocation getLocation() {
        return location;
    }

    public void setLocation(TblLocation location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (branchId != null ? branchId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblBranch)) {
            return false;
        }
        TblBranch other = (TblBranch) object;
        if ((this.branchId == null && other.branchId != null) || (this.branchId != null && !this.branchId.equals(other.branchId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblBranch[ id=" + branchId + " ]";
    }
    
}
