/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_global_setting")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblGlobalSetting.findAll", query = "SELECT t FROM TblGlobalSetting t")
    , @NamedQuery(name = "TblGlobalSetting.findById", query = "SELECT t FROM TblGlobalSetting t WHERE t.id = :id")
    , @NamedQuery(name = "TblGlobalSetting.findByEnterpriseId", query = "SELECT t FROM TblGlobalSetting t WHERE t.enterpriseId = :enterpriseId")
    , @NamedQuery(name = "TblGlobalSetting.findBySettingType", query = "SELECT t FROM TblGlobalSetting t WHERE t.settingType = :settingType")
    , @NamedQuery(name = "TblGlobalSetting.findBySettingJson", query = "SELECT t FROM TblGlobalSetting t WHERE t.settingJson = :settingJson")
    , @NamedQuery(name = "TblGlobalSetting.findByCreatedOn", query = "SELECT t FROM TblGlobalSetting t WHERE t.createdOn = :createdOn")
    , @NamedQuery(name = "TblGlobalSetting.findByCreatedBy", query = "SELECT t FROM TblGlobalSetting t WHERE t.createdBy = :createdBy")
    , @NamedQuery(name = "TblGlobalSetting.findByUpdatedOn", query = "SELECT t FROM TblGlobalSetting t WHERE t.updatedOn = :updatedOn")
    , @NamedQuery(name = "TblGlobalSetting.findByUpdatedBy", query = "SELECT t FROM TblGlobalSetting t WHERE t.updatedBy = :updatedBy")})
public class TblGlobalSetting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "enterprise_id")
    private Long enterpriseId;
    @Size(max = 20)
    @Column(name = "setting_type")
    private String settingType;
    @Size(max = 2147483647)
    @Column(name = "setting_json")
    private String settingJson;
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

    public TblGlobalSetting() {
    }

    public TblGlobalSetting(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getSettingType() {
        return settingType;
    }

    public void setSettingType(String settingType) {
        this.settingType = settingType;
    }

    public String getSettingJson() {
        return settingJson;
    }

    public void setSettingJson(String settingJson) {
        this.settingJson = settingJson;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblGlobalSetting)) {
            return false;
        }
        TblGlobalSetting other = (TblGlobalSetting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblGlobalSetting[ id=" + id + " ]";
    }
    
}
