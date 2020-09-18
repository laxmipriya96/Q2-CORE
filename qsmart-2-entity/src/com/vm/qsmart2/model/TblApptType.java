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
 * @author Phani
 */
@Entity
@Table(name = "tbl_appt_type")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "TblApptType.findAll", query = "SELECT t FROM TblApptType t")
//    , @NamedQuery(name = "TblApptType.findById", query = "SELECT t FROM TblApptType t WHERE t.id = :id")
//    , @NamedQuery(name = "TblApptType.findByLocationId", query = "SELECT t FROM TblApptType t WHERE t.locationId = :locationId")
//    , @NamedQuery(name = "TblApptType.findByApptType", query = "SELECT t FROM TblApptType t WHERE t.apptType = :apptType")
//    , @NamedQuery(name = "TblApptType.findByApptCode", query = "SELECT t FROM TblApptType t WHERE t.apptCode = :apptCode")
//    , @NamedQuery(name = "TblApptType.findByCreatedOn", query = "SELECT t FROM TblApptType t WHERE t.createdOn = :createdOn")
//    , @NamedQuery(name = "TblApptType.findByCreatedBy", query = "SELECT t FROM TblApptType t WHERE t.createdBy = :createdBy")
//    , @NamedQuery(name = "TblApptType.findByUpdatedOn", query = "SELECT t FROM TblApptType t WHERE t.updatedOn = :updatedOn")
//    , @NamedQuery(name = "TblApptType.findByUpdatedBy", query = "SELECT t FROM TblApptType t WHERE t.updatedBy = :updatedBy")})
public class TblApptType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long apptTypeId;
    @Column(name = "location_id")
    private Long locationId;
    @Column(name = "appt_type")
    private String apptType;
    @Column(name = "appt_code")
    private String apptCode;
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
    
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.ALL
            },
            mappedBy = "apptTypes")
    private Set<TblService> services;
    
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_user_appt_type", joinColumns = {
        @JoinColumn(name = "appt_type_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Set<TblUser> users;

    public TblApptType() {
    }

    public TblApptType(Long id) {
        this.apptTypeId = id;
    }

    public Set<TblService> getServices() {
        return services;
    }

    public void setServices(Set<TblService> services) {
        this.services = services;
    }

    
    public Long getApptTypeId() {
        return apptTypeId;
    }

    
    public void setApptTypeId(Long apptTypeId) {
        this.apptTypeId = apptTypeId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    

    public String getApptType() {
        return apptType;
    }

    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

    public String getApptCode() {
        return apptCode;
    }

    public void setApptCode(String apptCode) {
        this.apptCode = apptCode;
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

    public Set<TblUser> getUsers() {
        return users;
    }

    public void setUsers(Set<TblUser> users) {
        this.users = users;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (apptTypeId != null ? apptTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblApptType)) {
            return false;
        }
        TblApptType other = (TblApptType) object;
        if ((this.apptTypeId == null && other.apptTypeId != null) || (this.apptTypeId != null && !this.apptTypeId.equals(other.apptTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblApptType[ id=" + apptTypeId + " ]";
    }
    
}
