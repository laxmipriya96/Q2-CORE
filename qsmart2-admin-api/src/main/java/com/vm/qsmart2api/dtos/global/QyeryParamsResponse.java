/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.global;

import java.util.List;

/**
 *
 * @author Phani
 */
public class QyeryParamsResponse {
    
    private List<ParamsDto> queryParams;

    public List<ParamsDto> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(List<ParamsDto> queryParams) {
        this.queryParams = queryParams;
    }

    public QyeryParamsResponse() {
    }

    public QyeryParamsResponse(List<ParamsDto> queryParams) {
        this.queryParams = queryParams;
    }

    @Override
    public String toString() {
        return "QyeryParamsResponse{" + "queryParams=" + queryParams + '}';
    }
    
}
