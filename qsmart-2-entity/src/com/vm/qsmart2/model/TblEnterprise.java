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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "tbl_enterprise")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblEnterprise.findAll", query = "SELECT t FROM TblEnterprise t")
    , @NamedQuery(name = "TblEnterprise.findById", query = "SELECT t FROM TblEnterprise t WHERE t.id = :id")
    , @NamedQuery(name = "TblEnterprise.findByEnterpriseNameEn", query = "SELECT t FROM TblEnterprise t WHERE t.enterpriseNameEn = :enterpriseNameEn")
    , @NamedQuery(name = "TblEnterprise.findByEnterpriseNameAr", query = "SELECT t FROM TblEnterprise t WHERE t.enterpriseNameAr = :enterpriseNameAr")
    , @NamedQuery(name = "TblEnterprise.findByTimeZoneId", query = "SELECT t FROM TblEnterprise t WHERE t.timeZoneId = :timeZoneId")
    , @NamedQuery(name = "TblEnterprise.findByLanguageId", query = "SELECT t FROM TblEnterprise t WHERE t.languageId = :languageId")
    , @NamedQuery(name = "TblEnterprise.findByLicenseFile", query = "SELECT t FROM TblEnterprise t WHERE t.licenseFile = :licenseFile")
    , @NamedQuery(name = "TblEnterprise.findByIsActive", query = "SELECT t FROM TblEnterprise t WHERE t.isActive = :isActive")
    , @NamedQuery(name = "TblEnterprise.findByCreatedBy", query = "SELECT t FROM TblEnterprise t WHERE t.createdBy = :createdBy")
    , @NamedQuery(name = "TblEnterprise.findByCreatedOn", query = "SELECT t FROM TblEnterprise t WHERE t.createdOn = :createdOn")
    , @NamedQuery(name = "TblEnterprise.findByUpdatedBy", query = "SELECT t FROM TblEnterprise t WHERE t.updatedBy = :updatedBy")
    , @NamedQuery(name = "TblEnterprise.findByUpdatedOn", query = "SELECT t FROM TblEnterprise t WHERE t.updatedOn = :updatedOn")})
public class TblEnterprise implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enterpriseId;
    @Basic(optional = false)
    @Column(name = "enterprise_name_en")
    private String enterpriseNameEn;
    @Column(name = "enterprise_name_ar")
    private String enterpriseNameAr;
    @Column(name = "time_zone_id")
    private Integer timeZoneId;
    @Column(name = "language_id")
    private Integer languageId;
    @Column(name = "license_file")
    private String licenseFile;
    
    @Column(name = "logo_file")
    private String logoPath;
    
    @Column(name = "is_active")
    private Integer isActive;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblenterprise")
    private Set<TblLocation> locations;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "enterprise")
    private Set<TblUser> users;
    
    @Lob
    @Column(name = "logo")
    private Byte[] logo;

    public Byte[] getLogo() {
        return logo;
    }

    public void setLogo(Byte[] logo) {
        this.logo = logo;
    }

    public TblEnterprise() {
    }

    public TblEnterprise(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    

    public TblEnterprise(long id, String enterpriseNameEn, Long createdBy, Date createdOn, Long updatedBy, Date updatedOn) {
        this.enterpriseId = id;
        this.enterpriseNameEn = enterpriseNameEn;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseNameEn() {
        return enterpriseNameEn;
    }

    public void setEnterpriseNameEn(String enterpriseNameEn) {
        this.enterpriseNameEn = enterpriseNameEn;
    }

    public String getEnterpriseNameAr() {
        return enterpriseNameAr;
    }

    public void setEnterpriseNameAr(String enterpriseNameAr) {
        this.enterpriseNameAr = enterpriseNameAr;
    }

    public Integer getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(Integer timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getLicenseFile() {
        return licenseFile;
    }

    public void setLicenseFile(String licenseFile) {
        this.licenseFile = licenseFile;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
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

    public Set<TblLocation> getLocations() {
        return locations;
    }

    public void setLocations(Set<TblLocation> locations) {
        this.locations = locations;
    }

    public Set<TblUser> getUsers() {
        return users;
    }

    public void setUsers(Set<TblUser> users) {
        this.users = users;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }
    
    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblEnterprise[ id=" + enterpriseId + " ]";
    }
    
}
