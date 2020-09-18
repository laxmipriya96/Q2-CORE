/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.model;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_permission")
@XmlRootElement
public class TblPermission implements Serializable {

    @Column(name = "permission")
    private String permission;
    
    
    @JoinColumn(name = "module_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TblModule module;
    
    @JoinColumn(name = "submodule_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TblSubModule subModule;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer permissionId;
    
    @ManyToMany
    @JoinTable(name = "tbl_role_permission", joinColumns = {
        @JoinColumn(name = "permission_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<TblRole> roles;

    public TblPermission() {
    }

    public TblPermission(Integer id) {
        this.permissionId = id;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public TblModule getModule() {
        return module;
    }

    public void setModule(TblModule module) {
        this.module = module;
    }

    public TblSubModule getSubModule() {
        return subModule;
    }

    public void setSubModule(TblSubModule subModule) {
        this.subModule = subModule;
    }
    

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Set<TblRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<TblRole> roles) {
        this.roles = roles;
    }
    
    
}
