/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "tbl_room")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "TblRoom.findAll", query = "SELECT t FROM TblRoom t")
//    , @NamedQuery(name = "TblRoom.findById", query = "SELECT t FROM TblRoom t WHERE t.id = :id")
//    , @NamedQuery(name = "TblRoom.findByRoomNo", query = "SELECT t FROM TblRoom t WHERE t.roomNo = :roomNo")
//    , @NamedQuery(name = "TblRoom.findByRoomMasterId", query = "SELECT t FROM TblRoom t WHERE t.roomMasterId = :roomMasterId")
//    , @NamedQuery(name = "TblRoom.findByCreatedBy", query = "SELECT t FROM TblRoom t WHERE t.createdBy = :createdBy")
//    , @NamedQuery(name = "TblRoom.findByCreatedOn", query = "SELECT t FROM TblRoom t WHERE t.createdOn = :createdOn")
//    , @NamedQuery(name = "TblRoom.findByUpdatedOn", query = "SELECT t FROM TblRoom t WHERE t.updatedOn = :updatedOn")
//    , @NamedQuery(name = "TblRoom.findByUpdatedBy", query = "SELECT t FROM TblRoom t WHERE t.updatedBy = :updatedBy")})
public class TblRoom implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long roomId;
    @Basic(optional = false)
    @Column(name = "room_no")
    private String roomNo;
    @Basic(optional = false)
    @Column(name = "created_by", updatable = false)
    private Long createdBy;
    @Column(name = "created_on", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;
    @Column(name = "updated_by")
    private Long updatedBy;
    @JoinColumn(name = "room_master_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TblRoomMaster roomMaster;
    
//    @ManyToMany
//    @JoinTable(name = "tbl_user_room", joinColumns = {
//        @JoinColumn(name = "room_id", referencedColumnName = "id")}, inverseJoinColumns = {
//        @JoinColumn(name = "user_id", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            },
            mappedBy = "rooms")
    private Set<TblUser> users;

    public TblRoom() {
    }

    public TblRoom(Long id) {
        this.roomId = id;
    }

    public Set<TblUser> getUsers() {
        return users;
    }

    public void setUsers(Set<TblUser> users) {
        this.users = users;
    }
    
    

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public TblRoomMaster getRoomMaster() {
        return roomMaster;
    }

    public void setRoomMaster(TblRoomMaster roomMaster) {
        this.roomMaster = roomMaster;
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
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblRoom)) {
            return false;
        }
        TblRoom other = (TblRoom) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblRoom[ id=" + roomId + " ]";
    }
    
}
