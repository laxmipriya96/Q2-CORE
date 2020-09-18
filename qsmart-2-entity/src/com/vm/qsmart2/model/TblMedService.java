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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tejasri
 */
@Entity
@Table(name = "tbl_med_service")
@XmlRootElement
public class TblMedService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medServiceId;
    @Column(name = "enterprise_id")
    private long enterpriseId;
    @Column(name = "location_id")
    private Long locationId;
    @Column(name = "med_service_name")
    private String medServiceName;
    @Column(name = "med_service_code")
    private String medServiceCode;
    @Column(name = "created_on", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "created_by", updatable = false)
    private Long createdBy;
    @Column(name = "updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;
    @Column(name = "updated_by")
    private Long updatedBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_user_med_service", joinColumns = {
        @JoinColumn(name = "med_service_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Set<TblUser> users;

    public TblMedService() {
    }

    public TblMedService(Long medServiceId) {
        this.medServiceId = medServiceId;
    }

    public Long getMedServiceId() {
        return medServiceId;
    }

    public void setMedServiceId(Long medServiceId) {
        this.medServiceId = medServiceId;
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

    public String getMedServiceName() {
        return medServiceName;
    }

    public void setMedServiceName(String medServiceName) {
        this.medServiceName = medServiceName;
    }

    public String getMedServiceCode() {
        return medServiceCode;
    }

    public void setMedServiceCode(String medServiceCode) {
        this.medServiceCode = medServiceCode;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (medServiceId != null ? medServiceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMedService)) {
            return false;
        }
        TblMedService other = (TblMedService) object;
        if ((this.medServiceId == null && other.medServiceId != null) || (this.medServiceId != null && !this.medServiceId.equals(other.medServiceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2api.utils.TblMedService[ id=" + medServiceId + " ]";
    }

    /**
     * @return the users
     */
    public Set<TblUser> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(Set<TblUser> users) {
        this.users = users;
    }

}
