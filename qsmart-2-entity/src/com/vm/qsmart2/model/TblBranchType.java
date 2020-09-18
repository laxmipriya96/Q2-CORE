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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_branch_type")
@XmlRootElement
public class TblBranchType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchTypeId;
    @Basic(optional = false)
    @Column(name = "branch_type_display")
    private String branchTypeDisplay;
    @Column(name = "branch_type_code")
    private String branchTypeCode;
    @OneToMany(mappedBy = "branchType")
    private Set<TblBranch> tblBranchCollection;

    public TblBranchType() {
    }

    public TblBranchType(Long branchTypeId) {
        this.branchTypeId = branchTypeId;
    }

    public TblBranchType(Long id, String branchTypeDisplay) {
        this.branchTypeId = branchTypeId;
        this.branchTypeDisplay = branchTypeDisplay;
    }

    public Long getBranchTypeId() {
        return branchTypeId;
    }

    public void setBranchTypeId(Long branchTypeId) {
        this.branchTypeId = branchTypeId;
    }

    public String getBranchTypeDisplay() {
        return branchTypeDisplay;
    }

    public void setBranchTypeDisplay(String branchTypeDisplay) {
        this.branchTypeDisplay = branchTypeDisplay;
    }

    public String getBranchTypeCode() {
        return branchTypeCode;
    }

    public void setBranchTypeCode(String branchTypeCode) {
        this.branchTypeCode = branchTypeCode;
    }

    @XmlTransient
    public Set<TblBranch> getTblBranchCollection() {
        return tblBranchCollection;
    }

    public void setTblBranchCollection(Set<TblBranch> tblBranchCollection) {
        this.tblBranchCollection = tblBranchCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (branchTypeId != null ? branchTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblBranchType)) {
            return false;
        }
        TblBranchType other = (TblBranchType) object;
        if ((this.branchTypeId == null && other.branchTypeId != null) || (this.branchTypeId != null && !this.branchTypeId.equals(other.branchTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblBranchType[ id=" + branchTypeId + " ]";
    }
    
}
