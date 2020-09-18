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
@Table(name = "tbl_module")
@XmlRootElement
public class TblModule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer moduleId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "module_name")
    private String moduleName;
    @Column(name = "sequence_no")
    private Integer sequenceNo;
    @Column(name = "module_type")
    private String moduleType;
    @Column(name = "link")
    private String link;
    @Column(name = "icon")
    private String icon;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "module")
    private Set<TblPermission> permissions;

    public TblModule() {
    }

    public TblModule(Integer id) {
        this.moduleId = id;
    }

    public TblModule(Integer id, String moduleName) {
        this.moduleId = id;
        this.moduleName = moduleName;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }


    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
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

    @Override
    public String toString() {
        return "TblModule{" + "moduleId=" + moduleId + ", moduleName=" + moduleName + ", sequenceNo=" + sequenceNo + ", moduleType=" + moduleType + ", link=" + link + ", icon=" + icon + '}';
    }

}
