/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author SOMANADH PHANI
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SResponse extends Response implements Serializable {

    private Object data;
    private int id;
    private Date timeStamp;

    public SResponse() {
    }

    public SResponse(boolean status, String messages) {
        super(status, messages);
    }

    public SResponse(boolean status, String messages, Object data, Date timeStamp) {
        super(status, messages);
        this.data = data;
        this.timeStamp = timeStamp;
    }

    public SResponse(boolean status, String messages, Object data, Date timeStamp, int id) {
        super(status, messages);
        this.data = data;
        this.timeStamp = timeStamp;
        this.id = id;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SResponse{" + "data=" + data + ", id=" + id + ", timeStamp=" + timeStamp + ", " + super.toString() + '}';
    }

}
