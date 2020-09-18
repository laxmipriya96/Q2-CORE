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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_kiosk")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblKiosk.findAll", query = "SELECT t FROM TblKiosk t")
    , @NamedQuery(name = "TblKiosk.findByEnterpriseId", query = "SELECT t FROM TblKiosk t WHERE t.enterpriseId = :enterpriseId")
    , @NamedQuery(name = "TblKiosk.findByLocationId", query = "SELECT t FROM TblKiosk t WHERE t.locationId = :locationId")
    , @NamedQuery(name = "TblKiosk.findByKioskName", query = "SELECT t FROM TblKiosk t WHERE t.kioskName = :kioskName")
    , @NamedQuery(name = "TblKiosk.findByKioskIp", query = "SELECT t FROM TblKiosk t WHERE t.kioskIp = :kioskIp")
    , @NamedQuery(name = "TblKiosk.findByKioskIdentifier", query = "SELECT t FROM TblKiosk t WHERE t.kioskIdentifier = :kioskIdentifier")
    , @NamedQuery(name = "TblKiosk.findByStatus", query = "SELECT t FROM TblKiosk t WHERE t.status = :status")
    , @NamedQuery(name = "TblKiosk.findByCreatedOn", query = "SELECT t FROM TblKiosk t WHERE t.createdOn = :createdOn")
    , @NamedQuery(name = "TblKiosk.findByCreatedBy", query = "SELECT t FROM TblKiosk t WHERE t.createdBy = :createdBy")
    , @NamedQuery(name = "TblKiosk.findByUpdatedOn", query = "SELECT t FROM TblKiosk t WHERE t.updatedOn = :updatedOn")
    , @NamedQuery(name = "TblKiosk.findByUpdatedBy", query = "SELECT t FROM TblKiosk t WHERE t.updatedBy = :updatedBy")})
public class TblKiosk implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long kioskId;
    @Column(name = "enterprise_id")
    private Long enterpriseId;
    @Column(name = "location_id")
    private Long locationId;
    @Basic(optional = false)
    @Column(name = "theme_id")
    private int themeId;
    @Column(name = "kiosk_name")
    private String kioskName;
    @Column(name = "kiosk_ip")
    private String kioskIp;
    @Column(name = "kiosk_identifier", updatable = false)
    private String kioskIdentifier;
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
    @JoinTable(name = "tbl_branch_kiosk", joinColumns = {
        @JoinColumn(name = "kiosk_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "branch_id", referencedColumnName = "id")})
    private Set<TblBranch> branches;

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }
    
    public TblKiosk() {
    }

    public TblKiosk(Long kioskId) {
        this.kioskId = kioskId;
    }

    public Set<TblBranch> getBranches() {
        return branches;
    }

    public void setBranches(Set<TblBranch> branches) {
        this.branches = branches;
    }
    
    public Long getKioskId() {
        return kioskId;
    }

    public void setKioskId(Long kioskId) {
        this.kioskId = kioskId;
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

    public String getKioskName() {
        return kioskName;
    }

    public void setKioskName(String kioskName) {
        this.kioskName = kioskName;
    }

    public String getKioskIp() {
        return kioskIp;
    }

    public void setKioskIp(String kioskIp) {
        this.kioskIp = kioskIp;
    }

    public String getKioskIdentifier() {
        return kioskIdentifier;
    }

    public void setKioskIdentifier(String kioskIdentifier) {
        this.kioskIdentifier = kioskIdentifier;
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
    public String toString() {
        return "com.vm.qsmart2.model.TblKiosk[ id=" + kioskId + " ]";
    }
    
}
