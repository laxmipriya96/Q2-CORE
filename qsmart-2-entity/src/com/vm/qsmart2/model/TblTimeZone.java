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
@Table(name = "tbl_time_zone")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblTimeZone.findAll", query = "SELECT t FROM TblTimeZone t")
    , @NamedQuery(name = "TblTimeZone.findById", query = "SELECT t FROM TblTimeZone t WHERE t.id = :id")
    , @NamedQuery(name = "TblTimeZone.findByCountryCode", query = "SELECT t FROM TblTimeZone t WHERE t.countryCode = :countryCode")
    , @NamedQuery(name = "TblTimeZone.findByCountryName", query = "SELECT t FROM TblTimeZone t WHERE t.countryName = :countryName")
    , @NamedQuery(name = "TblTimeZone.findByTimeZone", query = "SELECT t FROM TblTimeZone t WHERE t.timeZone = :timeZone")
    , @NamedQuery(name = "TblTimeZone.findByGmtOffset", query = "SELECT t FROM TblTimeZone t WHERE t.gmtOffset = :gmtOffset")})
public class TblTimeZone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "country_code")
    private String countryCode;
    @Column(name = "country_name")
    private String countryName;
    @Column(name = "time_zone")
    private String timeZone;
    @Column(name = "gmt_offset")
    private String gmtOffset;

    public TblTimeZone() {
    }

    public TblTimeZone(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getGmtOffset() {
        return gmtOffset;
    }

    public void setGmtOffset(String gmtOffset) {
        this.gmtOffset = gmtOffset;
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
        if (!(object instanceof TblTimeZone)) {
            return false;
        }
        TblTimeZone other = (TblTimeZone) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vm.qsmart2.model.TblTimeZone[ id=" + id + " ]";
    }
    
}
