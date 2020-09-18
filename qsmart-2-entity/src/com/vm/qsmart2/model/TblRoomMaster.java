/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
//import org.hibernate.mapping.Set;

/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_room_master")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "TblRoomMaster.findAll", query = "SELECT t FROM TblRoomMaster t")
//    , @NamedQuery(name = "TblRoomMaster.findById", query = "SELECT t FROM TblRoomMaster t WHERE t.id = :id")
//    , @NamedQuery(name = "TblRoomMaster.findByRoomNameEn", query = "SELECT t FROM TblRoomMaster t WHERE t.roomNameEn = :roomNameEn")
//    , @NamedQuery(name = "TblRoomMaster.findByRoomNameAr", query = "SELECT t FROM TblRoomMaster t WHERE t.roomNameAr = :roomNameAr")
//    , @NamedQuery(name = "TblRoomMaster.findByMaxAllowedToken", query = "SELECT t FROM TblRoomMaster t WHERE t.maxAllowedToken = :maxAllowedToken")
//    , @NamedQuery(name = "TblRoomMaster.findByBranchId", query = "SELECT t FROM TblRoomMaster t WHERE t.branchId = :branchId")
//    , @NamedQuery(name = "TblRoomMaster.findByCreatedBy", query = "SELECT t FROM TblRoomMaster t WHERE t.createdBy = :createdBy")
//    , @NamedQuery(name = "TblRoomMaster.findByCreatedOn", query = "SELECT t FROM TblRoomMaster t WHERE t.createdOn = :createdOn")
//    , @NamedQuery(name = "TblRoomMaster.findByUpdatedBy", query = "SELECT t FROM TblRoomMaster t WHERE t.updatedBy = :updatedBy")
//    , @NamedQuery(name = "TblRoomMaster.findByUpdatedOn", query = "SELECT t FROM TblRoomMaster t WHERE t.updatedOn = :updatedOn")})
public class TblRoomMaster implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long roomMasterId;
    @Basic(optional = false)
    @Column(name = "room_name_en")
    private String roomNameEn;
    @Basic(optional = false)
    @Column(name = "room_name_ar")
    private String roomNameAr;
    @Column(name = "max_allowed_token")
    private Integer maxAllowedToken;
    @Basic(optional = false)
    @Column(name = "branch_id", updatable = false)
    private Long branchId;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomMaster")
    private Collection<TblRoom> rooms;


    public TblRoomMaster() {
    }

    public TblRoomMaster(Long id) {
        this.roomMasterId = id;
    }

    public Collection<TblRoom> getRooms() {
        return rooms;
    }

    public void setRooms(Collection<TblRoom> rooms) {
        this.rooms = rooms;
    }

    public Long getRoomMasterId() {
        return roomMasterId;
    }

    public void setRoomMasterId(Long roomMasterId) {
        this.roomMasterId = roomMasterId;
    }

    public String getRoomNameEn() {
        return roomNameEn;
    }

    public void setRoomNameEn(String roomNameEn) {
        this.roomNameEn = roomNameEn;
    }

    public String getRoomNameAr() {
        return roomNameAr;
    }

    public void setRoomNameAr(String roomNameAr) {
        this.roomNameAr = roomNameAr;
    }

    public Integer getMaxAllowedToken() {
        return maxAllowedToken;
    }

    public void setMaxAllowedToken(Integer maxAllowedToken) {
        this.maxAllowedToken = maxAllowedToken;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
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
        hash += (roomMasterId != null ? roomMasterId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblRoomMaster)) {
            return false;
        }
        TblRoomMaster other = (TblRoomMaster) object;
        if ((this.roomMasterId == null && other.roomMasterId != null) || (this.roomMasterId != null && !this.roomMasterId.equals(other.roomMasterId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblRoomMaster[ id=" + roomMasterId + " ]";
    }

}
