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
@Table(name = "tbl_role")
@XmlRootElement
public class TblRole implements Serializable {

    private static final long serialVersionUID = 1L;
   
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long roleId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "role_name")
    private String roleName;
    @Basic(optional = false)
    @Column(name = "role_code", updatable = false)
    private String roleCode;
    @Column(name = "role_type")
    private Integer roleType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_custom_role", updatable = false)
    private short isCustomRole;
    @Column(name = "enterprise_id", updatable = false)
    private Integer enterpriseId;
    
    @Column(name = "branch_type", updatable = false)
    private Integer branchType;
    @Basic(optional = false)
    @Column(name = "created_on", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "created_by", updatable = false)
    private Long createdBy;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated_by")
    private Long updatedBy;

    public TblRole() {
    }

    public TblRole(long id) {
        this.roleId = id;
    }

    public TblRole(long id, String roleName, String roleCode, short isCustomRole, Date createdOn, Long createdBy, Date updatedOn, Long updatedBy) {
        this.roleId = id;
        this.roleName = roleName;
        this.roleCode = roleCode;
        this.isCustomRole = isCustomRole;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.updatedOn = updatedOn;
        this.updatedBy = updatedBy;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public short getIsCustomRole() {
        return isCustomRole;
    }

    public void setIsCustomRole(short isCustomRole) {
        this.isCustomRole = isCustomRole;
    }

    public Integer getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Integer enterpriseId) {
        this.enterpriseId = enterpriseId;
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

    public Integer getBranchType() {
        return branchType;
    }

    public void setBranchType(Integer branchType) {
        this.branchType = branchType;
    }
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_user_role", joinColumns = {
        @JoinColumn(name = "role_id", referencedColumnName = "id", updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false)})
    @JsonIgnore
    private Set<TblUser> users;
    
    
    @ManyToMany
    @JoinTable(name = "tbl_role_permission", joinColumns = {
        @JoinColumn(name = "role_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "permission_id", referencedColumnName = "id")})
    @JsonIgnore
    private Set<TblPermission> permissions;

    public Set<TblUser> getUsers() {
        return users;
    }

    public void setUsers(Set<TblUser> users) {
        this.users = users;
    }

    public Set<TblPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<TblPermission> permissions) {
        this.permissions = permissions;
    }
    
    

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblRole[ id=" + roleId + " ]";
    }

}
