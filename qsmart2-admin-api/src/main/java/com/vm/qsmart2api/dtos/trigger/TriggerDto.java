/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.trigger;

import com.vm.qsmart2api.dtos.params.ParamDto;
import java.util.List;

/**
 *
 * @author Tejasri
 */
public class TriggerDto {
    private Long triggerId;
    private String triggerName;
    private String triggerCode;
    
    private List<ParamDto> params;

    public List<ParamDto> getParams() {
        return params;
    }

    public void setParams(List<ParamDto> params) {
        this.params = params;
    }
    
    public TriggerDto() {
    }

    public TriggerDto(Long triggerId, String triggerName, String triggerCode) {
        this.triggerId = triggerId;
        this.triggerName = triggerName;
        this.triggerCode = triggerCode;
    }

    public Long getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(Long triggerId) {
        this.triggerId = triggerId;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerCode() {
        return triggerCode;
    }

    public void setTriggerCode(String triggerCode) {
        this.triggerCode = triggerCode;
    }

    @Override
    public String toString() {
        return "TriggerDto{" + "triggerId=" + triggerId + ", triggerName=" + triggerName + ", triggerCode=" + triggerCode + '}';
    }
    
    
}
