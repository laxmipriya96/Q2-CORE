/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.menus;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Phani
 */
public class ModulesResponse {

    private List<ModulesDto> modules = new ArrayList<>();

    public void addModules(ModulesDto module) {
        this.modules.add(module);
    }

    public List<ModulesDto> getModules() {
        return modules;
    }

    public void setModules(List<ModulesDto> modules) {
        this.modules = modules;
    }

    public ModulesResponse() {
    }

    public ModulesResponse(List<ModulesDto> modules) {
        this.modules = modules;
    }

    @Override
    public String toString() {
        return "ModulesResponse{" + "modules=" + modules + '}';
    }

}
