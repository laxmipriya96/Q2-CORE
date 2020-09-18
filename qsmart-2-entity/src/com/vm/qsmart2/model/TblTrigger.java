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
@Table(name = "tbl_trigger")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "TblTrigger.findAll", query = "SELECT t FROM TblTrigger t")
//    , @NamedQuery(name = "TblTrigger.findById", query = "SELECT t FROM TblTrigger t WHERE t.id = :id")
//    , @NamedQuery(name = "TblTrigger.findByTriggerName", query = "SELECT t FROM TblTrigger t WHERE t.triggerName = :triggerName")
//    , @NamedQuery(name = "TblTrigger.findByTriggerCode", query = "SELECT t FROM TblTrigger t WHERE t.triggerCode = :triggerCode")})
public class TblTrigger implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long triggerId;
    @Column(name = "trigger_name")
    private String triggerName;
    @Column(name = "trigger_code")
    private String triggerCode;
    
    @ManyToMany
    @JoinTable(name = "tbl_trigger_params", joinColumns = {
        @JoinColumn(name = "trigger_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "param_id", referencedColumnName = "id")})
    private Set<TblParams> params;

    public TblTrigger() {
    }

    public TblTrigger(Long triggerId) {
        this.triggerId = triggerId;
    }

    public Long getTriggerId() {
        return triggerId;
    }

    public Set<TblParams> getParams() {
        return params;
    }

    public void setParams(Set<TblParams> params) {
        this.params = params;
    }
    
    

    public void setTriggerId(Long triggerId) {
        this.triggerId = triggerId;
    }

   

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerCode() {
        return triggerCode;
    }

    public void setTriggerCode(String triggerCode) {
        this.triggerCode = triggerCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (triggerId != null ? triggerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblTrigger)) {
            return false;
        }
        TblTrigger other = (TblTrigger) object;
        if ((this.triggerId == null && other.triggerId != null) || (this.triggerId != null && !this.triggerId.equals(other.triggerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblTrigger[ id=" + triggerId + " ]";
    }
    
}
