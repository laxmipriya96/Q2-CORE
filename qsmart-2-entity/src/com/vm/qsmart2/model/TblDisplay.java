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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tejasri
 */
@Entity
@Table(name = "tbl_display")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "TblDisplay.findAll", query = "SELECT t FROM TblDisplay t")
//    , @NamedQuery(name = "TblDisplay.findById", query = "SELECT t FROM TblDisplay t WHERE t.id = :id")
//    , @NamedQuery(name = "TblDisplay.findByEnterpriseId", query = "SELECT t FROM TblDisplay t WHERE t.enterpriseId = :enterpriseId")
//    , @NamedQuery(name = "TblDisplay.findByLocationId", query = "SELECT t FROM TblDisplay t WHERE t.locationId = :locationId")
//    , @NamedQuery(name = "TblDisplay.findByDisplayName", query = "SELECT t FROM TblDisplay t WHERE t.displayName = :displayName")
//    , @NamedQuery(name = "TblDisplay.findByThemeId", query = "SELECT t FROM TblDisplay t WHERE t.themeId = :themeId")
//    , @NamedQuery(name = "TblDisplay.findByDisplayIdentifier", query = "SELECT t FROM TblDisplay t WHERE t.displayIdentifier = :displayIdentifier")
//    , @NamedQuery(name = "TblDisplay.findByStatus", query = "SELECT t FROM TblDisplay t WHERE t.status = :status")
//    , @NamedQuery(name = "TblDisplay.findByCreatedOn", query = "SELECT t FROM TblDisplay t WHERE t.createdOn = :createdOn")
//    , @NamedQuery(name = "TblDisplay.findByCreatedBy", query = "SELECT t FROM TblDisplay t WHERE t.createdBy = :createdBy")
//    , @NamedQuery(name = "TblDisplay.findByUpdatedOn", query = "SELECT t FROM TblDisplay t WHERE t.updatedOn = :updatedOn")
//    , @NamedQuery(name = "TblDisplay.findByUpdatedBy", query = "SELECT t FROM TblDisplay t WHERE t.updatedBy = :updatedBy")})
public class TblDisplay implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long displayId;
    @Basic(optional = false)
    @Column(name = "enterprise_id")
    private Long enterpriseId;
    @Basic(optional = false)
    @Column(name = "location_id")
    private Long locationId;
    @Column(name = "display_name")
    private String displayName;
    @Basic(optional = false)
    @Column(name = "theme_id")
    private Integer themeId;
    @Column(name = "display_identifier", updatable = false)
    private String displayIdentifier;
    @Column(name = "status")
    private Integer status;
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
    
    @ManyToMany
    @JoinTable(name = "tbl_service_display", joinColumns = {
        @JoinColumn(name = "display_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "service_id", referencedColumnName = "id")})
    private Set<TblService> services;

    public TblDisplay() {
    }

    public TblDisplay(Long id) {
        this.displayId = id;
    }

    public Set<TblService> getServices() {
        return services;
    }

    public void setServices(Set<TblService> services) {
        this.services = services;
    }
    

    public TblDisplay(Long id, Long enterpriseId, Long locationId, Integer themeId) {
        this.displayId = id;
        this.enterpriseId = enterpriseId;
        this.locationId = locationId;
        this.themeId = themeId;
    }

    public Long getDisplayId() {
        return displayId;
    }

    public void setDisplayId(Long displayId) {
        this.displayId = displayId;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }

    public String getDisplayIdentifier() {
        return displayIdentifier;
    }

    public void setDisplayIdentifier(String displayIdentifier) {
        this.displayIdentifier = displayIdentifier;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        hash += (displayId != null ? displayId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblDisplay)) {
            return false;
        }
        TblDisplay other = (TblDisplay) object;
        if ((this.displayId == null && other.displayId != null) || (this.displayId != null && !this.displayId.equals(other.displayId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblDisplay[ id=" + displayId + " ]";
    }
    
}
