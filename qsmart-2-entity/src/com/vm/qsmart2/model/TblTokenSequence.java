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
@Table(name = "tbl_token_sequence")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblTokenSequence.findAll", query = "SELECT t FROM TblTokenSequence t")
    , @NamedQuery(name = "TblTokenSequence.findByServiceId", query = "SELECT t FROM TblTokenSequence t WHERE t.serviceId = :serviceId")
    , @NamedQuery(name = "TblTokenSequence.findByTokenSeq", query = "SELECT t FROM TblTokenSequence t WHERE t.tokenSeq = :tokenSeq")})
public class TblTokenSequence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long tokenSequenceId;
    @Column(name = "service_id")
    private Long serviceId;
    @Column(name = "token_seq")
    private Integer tokenSeq;

    public TblTokenSequence() {
    }

    public TblTokenSequence(Long tokenSequenceId) {
        this.tokenSequenceId = tokenSequenceId;
    }


    public Integer getTokenSeq() {
        return tokenSeq;
    }

    public void setTokenSeq(Integer tokenSeq) {
        this.tokenSeq = tokenSeq;
    }

    public Long getTokenSequenceId() {
        return tokenSequenceId;
    }

    public void setTokenSequenceId(Long tokenSequenceId) {
        this.tokenSequenceId = tokenSequenceId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "TblTokenSequence{" + "tokenSequenceId=" + tokenSequenceId + ", serviceId=" + serviceId + ", tokenSeq=" + tokenSeq + '}';
    }

}
