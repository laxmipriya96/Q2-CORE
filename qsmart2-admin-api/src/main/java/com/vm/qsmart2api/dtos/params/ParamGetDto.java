/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.params;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class ParamGetDto {
    private List<ParamDto> params;

    public ParamGetDto() {
    }

    public ParamGetDto(List<ParamDto> params) {
        this.params = params;
    }

    public List<ParamDto> getParams() {
        return params;
    }

    public void setParams(List<ParamDto> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "ParamGetDto{" + "params=" + params + '}';
    }
    
    
}
