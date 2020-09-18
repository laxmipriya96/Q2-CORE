/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.params;

/**
 *
 * @author Tejasri
 */
public class ParamDto {
    private Long paramId;
    private String paramName;
    private String paramCode;

    public ParamDto() {
    }

    public ParamDto(Long paramId, String paramName, String paramCode) {
        this.paramId = paramId;
        this.paramName = paramName;
        this.paramCode = paramCode;
    }

    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
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
    public String toString() {
        return "ParamDto{" + "paramId=" + paramId + ", paramName=" + paramName + ", paramCode=" + paramCode + '}';
    }
    
    
}
