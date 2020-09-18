/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.utils;

/**
 *
 * @author Ashok
 */
public class TriggerTypeDTO {

    private String tranId;
    private String triggerType;
    private Long serviceId;

    public TriggerTypeDTO() {
    }

    public TriggerTypeDTO(String tranId, String triggerType, Long serviceId) {
        this.tranId = tranId;
        this.triggerType = triggerType;
        this.serviceId = serviceId;
    }
    
    

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

}
