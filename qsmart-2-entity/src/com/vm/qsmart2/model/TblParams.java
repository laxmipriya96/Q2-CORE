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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tejasri
 */
@Entity
@Table(name = "tbl_params")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "TblParams.findAll", query = "SELECT t FROM TblParams t")
//    , @NamedQuery(name = "TblParams.findById", query = "SELECT t FROM TblParams t WHERE t.id = :id")
//    , @NamedQuery(name = "TblParams.findByParamName", query = "SELECT t FROM TblParams t WHERE t.paramName = :paramName")
//    , @NamedQuery(name = "TblParams.findByParamCode", query = "SELECT t FROM TblParams t WHERE t.paramCode = :paramCode")})
public class TblParams implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long paramId;
    @Column(name = "param_name")
    private String paramName;
    @Column(name = "param_code")
    private String paramCode;
    
    @ManyToMany
    @JoinTable(name = "tbl_trigger_params", joinColumns = {
        @JoinColumn(name = "param_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "trigger_id", referencedColumnName = "id")})
    private Set<TblTrigger> triggers;

    public TblParams() {
    }

    public TblParams(Long paramId) {
        this.paramId = paramId;
    }

    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }

    public Set<TblTrigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(Set<TblTrigger> triggers) {
        this.triggers = triggers;
    }


    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paramId != null ? paramId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblParams)) {
            return false;
        }
        TblParams other = (TblParams) object;
        if ((this.paramId == null && other.paramId != null) || (this.paramId != null && !this.paramId.equals(other.paramId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblParams[ id=" + paramId + " ]";
    }
    
}
