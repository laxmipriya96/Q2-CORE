/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "tbl_service_location")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblServiceLocation.findAll", query = "SELECT t FROM TblServiceLocation t")
    , @NamedQuery(name = "TblServiceLocation.findById", query = "SELECT t FROM TblServiceLocation t WHERE t.id = :id")
    , @NamedQuery(name = "TblServiceLocation.findByLocationId", query = "SELECT t FROM TblServiceLocation t WHERE t.locationId = :locationId")
    , @NamedQuery(name = "TblServiceLocation.findByServiceLocation", query = "SELECT t FROM TblServiceLocation t WHERE t.serviceLocation = :serviceLocation")
    , @NamedQuery(name = "TblServiceLocation.findByCreatedOn", query = "SELECT t FROM TblServiceLocation t WHERE t.createdOn = :createdOn")})
public class TblServiceLocation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceLocationId;
    @Basic(optional = false)
    @Column(name = "location_id")
    private Long locationId;
    @Basic(optional = false)
    @Column(name = "service_location")
    private String serviceLocation;
    @Basic(optional = false)
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    
     @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_branch_service_location", joinColumns = {
        @JoinColumn(name = "service_location_id", referencedColumnName = "id", updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "branch_id", referencedColumnName = "id", updatable = false)})
    @JsonIgnore
    private Set<TblBranch> branches;
    

    public TblServiceLocation() {
    }

    public TblServiceLocation(Long serviceLocationId) {
        this.serviceLocationId = serviceLocationId;
    }

    public Long getServiceLocationId() {
        return serviceLocationId;
    }

    public void setServiceLocationId(Long serviceLocationId) {
        this.serviceLocationId = serviceLocationId;
    }

    public Set<TblBranch> getBranches() {
        return branches;
    }

    public void setBranches(Set<TblBranch> branches) {
        this.branches = branches;
    }


    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getServiceLocation() {
        return serviceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "TblServiceLocation{" + "serviceLocationId=" + serviceLocationId + '}';
    }
    
}
