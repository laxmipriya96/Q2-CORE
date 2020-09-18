/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.global;

/**
 *
 * @author Phani
 */
public class ParamsDto {
    
    private int queryParamId;
    private String queryParam;

    public ParamsDto() {
    }

    public ParamsDto(int queryParamId, String queryParam) {
        this.queryParamId = queryParamId;
        this.queryParam = queryParam;
    }
    
    public int getQueryParamId() {
        return queryParamId;
    }

    public void setQueryParamId(int queryParamId) {
        this.queryParamId = queryParamId;
    }

    public String getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }

    @Override
    public String toString() {
        return "QueryParams{" + "queryParamId=" + queryParamId + ", queryParam=" + queryParam + '}';
    }
    
}
