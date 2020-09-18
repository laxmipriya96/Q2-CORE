/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_sub_module")
@XmlRootElement
public class TblSubModule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer subModuleId;
    @Column(name = "sub_module_name")
    private String subModuleName;
    @Column(name = "sequence_no")
    private Integer sequenceNo;
    @Column(name = "branch_type")
    private Integer branchType;
    @Column(name = "link")
    private String link;
    @Column(name = "icon")
    private String icon;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subModule")
    private Set<TblPermission> permissions;

    public TblSubModule() {
    }

    public TblSubModule(Integer id) {
        this.subModuleId = id;
    }

    public Integer getSubModuleId() {
        return subModuleId;
    }

    public void setSubModuleId(Integer subModuleId) {
        this.subModuleId = subModuleId;
    }

   

    public String getSubModuleName() {
        return subModuleName;
    }

    public void setSubModuleName(String subModuleName) {
        this.subModuleName = subModuleName;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Integer getBranchType() {
        return branchType;
    }

    public void setBranchType(Integer branchType) {
        this.branchType = branchType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Set<TblPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<TblPermission> permissions) {
        this.permissions = permissions;
    }

   
    
}
