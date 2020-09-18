/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tejasri
 */
@Entity
@Table(name = "tbl_theme")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "TblTheme.findAll", query = "SELECT t FROM TblTheme t")
//    , @NamedQuery(name = "TblTheme.findById", query = "SELECT t FROM TblTheme t WHERE t.id = :id")
//    , @NamedQuery(name = "TblTheme.findByThemeName", query = "SELECT t FROM TblTheme t WHERE t.themeName = :themeName")
//    , @NamedQuery(name = "TblTheme.findByThemeType", query = "SELECT t FROM TblTheme t WHERE t.themeType = :themeType")
//    , @NamedQuery(name = "TblTheme.findByIsDefault", query = "SELECT t FROM TblTheme t WHERE t.isDefault = :isDefault")})
public class TblTheme implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long themeId;
    @Column(name = "theme_name")
    private String themeName;
    @Column(name = "theme_type")
    private String themeType;
    @Column(name = "is_default")
    private Short isDefault;

    public TblTheme() {
    }

    public TblTheme(Long id) {
        this.themeId = id;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeType() {
        return themeType;
    }

    public void setThemeType(String themeType) {
        this.themeType = themeType;
    }

    public Short getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Short isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (themeId != null ? themeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblTheme)) {
            return false;
        }
        TblTheme other = (TblTheme) object;
        if ((this.themeId == null && other.themeId != null) || (this.themeId != null && !this.themeId.equals(other.themeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblTheme[ id=" + themeId + " ]";
    }
    
}
