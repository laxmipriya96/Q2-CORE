/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_user")
@XmlRootElement
public class TblUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "hash_password", updatable = false)
    private String hashPassword;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email_id")
    private String emailId;
    @Column(name = "contact_no")
    private String contactNo;
    @Column(name = "user_type")
    private String userType;
    @Column(name = "is_active")
    private short isActive;
    @Column(name = "system_access")
    private Short systemAccess;
    @Column(name = "is_first_login")
    private Short isFirstLogin;
    @Column(name = "resource_code")
    private String resourceCode;

    @JoinColumn(name = "enterprise_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TblEnterprise enterprise;

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
    @JoinTable(name = "tbl_user_role", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<TblRole> roles;

    @ManyToMany
    @JoinTable(name = "tbl_user_location", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "location_id", referencedColumnName = "id")})
    private Set<TblLocation> locations;

//    @ManyToMany
//    @JoinTable(name = "tbl_user_med_service", joinColumns = {
//        @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
//        @JoinColumn(name = "med_service_id", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.ALL
            },
            mappedBy = "users")
    private Set<TblMedService> medServices;
    
//   @ManyToMany
//    @JoinTable(name = "tbl_user_room", joinColumns = {
//        @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
//        @JoinColumn(name = "room_id", referencedColumnName = "id")})
   
    
    
    @ManyToMany
    @JoinTable(name = "tbl_user_room", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "room_id", referencedColumnName = "id")})
    private Set<TblRoom> rooms;
    
    
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private Set<TblApptType> apptTypes;

    public Set<TblRoom> getRooms() {
        return rooms;
    }

    public void setRooms(Set<TblRoom> rooms) {
        this.rooms = rooms;
    }

    
    public Set<TblMedService> getMedServices() {
        return medServices;
    }

    public void setMedServices(Set<TblMedService> medServices) {
        this.medServices = medServices;
    }

    public Set<TblLocation> getLocations() {
        return locations;
    }

    public void setLocations(Set<TblLocation> locations) {
        this.locations = locations;
    }

    public TblUser() {
    }

    public TblUser(long id) {
        this.userId = id;
    }

    public TblUser(long id, String userName, String hashPassword, String firstName, String lastName, String emailId, String contactNo, String userType, short isActive, String resourceCode, Date createdOn, Long createdBy, Date updatedOn, Long updatedBy) {
        this.userId = id;
        this.userName = userName;
        this.hashPassword = hashPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.contactNo = contactNo;
        this.userType = userType;
        this.isActive = isActive;
        this.resourceCode = resourceCode;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.updatedOn = updatedOn;
        this.updatedBy = updatedBy;
    }

    public Set<TblRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<TblRole> roles) {
        this.roles = roles;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public short getIsActive() {
        return isActive;
    }

    public void setIsActive(short isActive) {
        this.isActive = isActive;
    }

    public Short getSystemAccess() {
        return systemAccess;
    }

    public void setSystemAccess(Short systemAccess) {
        this.systemAccess = systemAccess;
    }

    public Short getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(Short isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public TblEnterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(TblEnterprise enterprise) {
        this.enterprise = enterprise;
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

    public Set<TblApptType> getApptTypes() {
        return apptTypes;
    }

    public void setApptTypes(Set<TblApptType> apptTypes) {
        this.apptTypes = apptTypes;
    }

    @Override
    public String toString() {
        return "TblUser{" + "enterprise_id=" + userId + '}';
    }

}
