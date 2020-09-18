/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.trigger;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class TriggerGetDto {

    private List<TriggerDto> triggers;

    public TriggerGetDto() {
    }

    public TriggerGetDto(List<TriggerDto> triggers) {
        this.triggers = triggers;
    }

    public List<TriggerDto> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<TriggerDto> triggers) {
        this.triggers = triggers;
    }

    @Override
    public String toString() {
        return "TrggerGetDto{" + "triggers=" + triggers + '}';
    }
}
