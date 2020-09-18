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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_scanner")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblScanner.findAll", query = "SELECT t FROM TblScanner t")
    , @NamedQuery(name = "TblScanner.findByEnterpriseId", query = "SELECT t FROM TblScanner t WHERE t.enterpriseId = :enterpriseId")
    , @NamedQuery(name = "TblScanner.findByLocationId", query = "SELECT t FROM TblScanner t WHERE t.locationId = :locationId")
    , @NamedQuery(name = "TblScanner.findByScannerName", query = "SELECT t FROM TblScanner t WHERE t.scannerName = :scannerName")
    , @NamedQuery(name = "TblScanner.findByScannerIp", query = "SELECT t FROM TblScanner t WHERE t.scannerIp = :scannerIp")
    , @NamedQuery(name = "TblScanner.findByStatus", query = "SELECT t FROM TblScanner t WHERE t.status = :status")
    , @NamedQuery(name = "TblScanner.findByScannerIdentifier", query = "SELECT t FROM TblScanner t WHERE t.scannerIdentifier = :scannerIdentifier")
    , @NamedQuery(name = "TblScanner.findByCreatedOn", query = "SELECT t FROM TblScanner t WHERE t.createdOn = :createdOn")
    , @NamedQuery(name = "TblScanner.findByCreatedBy", query = "SELECT t FROM TblScanner t WHERE t.createdBy = :createdBy")
    , @NamedQuery(name = "TblScanner.findByUpdatedOn", query = "SELECT t FROM TblScanner t WHERE t.updatedOn = :updatedOn")
    , @NamedQuery(name = "TblScanner.findByUpdatedBy", query = "SELECT t FROM TblScanner t WHERE t.updatedBy = :updatedBy")})
public class TblScanner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long scannerId;
    @Basic(optional = false)
    @Column(name = "enterprise_id")
    private Long enterpriseId;
    @Basic(optional = false)
    @Column(name = "location_id")
    private Long locationId;
    @Column(name = "scanner_name")
    private String scannerName;
    @Column(name = "scanner_ip")
    private String scannerIp;
    @Column(name = "status")
    private Integer status;
    @Column(name = "scanner_identifier")
    private String scannerIdentifier;
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
    @JoinTable(name = "tbl_branch_scanner", joinColumns = {
        @JoinColumn(name = "scanner_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "branch_id", referencedColumnName = "id")})
    private Set<TblBranch> branches;
    

    public TblScanner() {
    }

    public TblScanner(Long scannerId) {
        this.scannerId = scannerId;
    }

    public TblScanner(Long scannerId, Long enterpriseId, Long locationId) {
        this.scannerId = scannerId;
        this.enterpriseId = enterpriseId;
        this.locationId = locationId;
    }

    public Long getScannerId() {
        return scannerId;
    }

    public void setScannerId(Long scannerId) {
        this.scannerId = scannerId;
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

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getScannerName() {
        return scannerName;
    }

    public void setScannerName(String scannerName) {
        this.scannerName = scannerName;
    }

    public String getScannerIp() {
        return scannerIp;
    }

    public void setScannerIp(String scannerIp) {
        this.scannerIp = scannerIp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getScannerIdentifier() {
        return scannerIdentifier;
    }

    public void setScannerIdentifier(String scannerIdentifier) {
        this.scannerIdentifier = scannerIdentifier;
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

    public Set<TblBranch> getBranches() {
        return branches;
    }

    public void setBranches(Set<TblBranch> branches) {
        this.branches = branches;
    }

  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scannerId != null ? scannerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblScanner)) {
            return false;
        }
        TblScanner other = (TblScanner) object;
        if ((this.scannerId == null && other.scannerId != null) || (this.scannerId != null && !this.scannerId.equals(other.scannerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblScanner[ id=" + scannerId + " ]";
    }
    
}
