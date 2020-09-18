/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.service;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class ServiceResponse {
    private String name;
    private List<ServiceCrtDto> children;

    public ServiceResponse() {
    }

    public ServiceResponse(String name, List<ServiceCrtDto> children) {
        this.name = name;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ServiceCrtDto> getChildren() {
        return children;
    }

    public void setChildren(List<ServiceCrtDto> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ServiceResponse{" + "name=" + name + ", children=" + children + '}';
    }
}
