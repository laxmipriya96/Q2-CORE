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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_location")
@XmlRootElement
public class TblLocation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long locationId;
    @Basic(optional = false)
    @Column(name = "location_name_en")
    private String locationNameEn;
    @Basic(optional = false)
    @Column(name = "location_name_ar")
    private String locationNameAr;
    @Column(name = "location_identfier")
    private String locationIdentfier;
    @Column(name = "location_address")
    private String locationAddress;
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
    @Column(name = "status")
    private int status;
    @JoinColumn(name = "enterprise_id", referencedColumnName = "id", updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TblEnterprise tblenterprise;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
    private Collection<TblBranch> tblBranchCollection;

    public TblLocation() {
    }

    public TblLocation(Long id) {
        this.locationId = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
    @ManyToMany
    @JoinTable(name = "tbl_user_location", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "location_id", referencedColumnName = "id")})
    private Set<TblUser> users;

    public Set<TblUser> getUsers() {
        return users;
    }

    public void setUsers(Set<TblUser> users) {
        this.users = users;
    }

    public TblLocation(Long id, String locationNameEn, String locationNameAr, Long createdBy, Date createdOn, Long updatedBy, Date updatedOn) {
        this.locationId = id;
        this.locationNameEn = locationNameEn;
        this.locationNameAr = locationNameAr;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationNameEn() {
        return locationNameEn;
    }

    public void setLocationNameEn(String locationNameEn) {
        this.locationNameEn = locationNameEn;
    }

    public String getLocationNameAr() {
        return locationNameAr;
    }

    public void setLocationNameAr(String locationNameAr) {
        this.locationNameAr = locationNameAr;
    }

    public String getLocationIdentfier() {
        return locationIdentfier;
    }

    public void setLocationIdentfier(String locationIdentfier) {
        this.locationIdentfier = locationIdentfier;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
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

    public TblEnterprise getTblenterprise() {
        return tblenterprise;
    }

    public void setTblenterprise(TblEnterprise tblenterprise) {
        this.tblenterprise = tblenterprise;
    }

    @XmlTransient
    public Collection<TblBranch> getTblBranchCollection() {
        return tblBranchCollection;
    }

    public void setTblBranchCollection(Collection<TblBranch> tblBranchCollection) {
        this.tblBranchCollection = tblBranchCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (locationId != null ? locationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblLocation)) {
            return false;
        }
        TblLocation other = (TblLocation) object;
        if ((this.locationId == null && other.locationId != null) || (this.locationId != null && !this.locationId.equals(other.locationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblLocation[ id=" + locationId + " ]";
    }

}
