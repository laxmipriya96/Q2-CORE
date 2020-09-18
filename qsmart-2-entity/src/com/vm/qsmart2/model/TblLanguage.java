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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phani
 */
@Entity
@Table(name = "tbl_language")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblLanguage.findAll", query = "SELECT t FROM TblLanguage t")
    , @NamedQuery(name = "TblLanguage.findById", query = "SELECT t FROM TblLanguage t WHERE t.id = :id")
    , @NamedQuery(name = "TblLanguage.findByLangCode", query = "SELECT t FROM TblLanguage t WHERE t.langCode = :langCode")
    , @NamedQuery(name = "TblLanguage.findByLanguage", query = "SELECT t FROM TblLanguage t WHERE t.language = :language")})
public class TblLanguage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "lang_code")
    private String langCode;
    @Column(name = "language")
    private String language;

    public TblLanguage() {
    }

    public TblLanguage(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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
        if (!(object instanceof TblLanguage)) {
            return false;
        }
        TblLanguage other = (TblLanguage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblLanguage[ id=" + id + " ]";
    }
    
}
