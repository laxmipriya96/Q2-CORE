/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.enterprise;

import java.util.List;

/**
 *
 * @author Phani
 */
public class EnterprisesDetails{
    
    private List<EnterpriseGDto> enterprises;

    public EnterprisesDetails() {
    }

    public EnterprisesDetails(List<EnterpriseGDto> enterprises) {
        this.enterprises = enterprises;
    }

    public List<EnterpriseGDto> getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(List<EnterpriseGDto> enterprises) {
        this.enterprises = enterprises;
    }

    @Override
    public String toString() {
        return "EntrpirsesDetails{" + "enterprises=" + enterprises + '}';
    }

    
    
}
