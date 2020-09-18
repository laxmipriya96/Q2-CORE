/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.service;

/**
 *
 * @author Tejasri
 */
public class NurseServingStats {
    private String name;
    private int count;

    public NurseServingStats() {
    }

    public NurseServingStats(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ServiceDetailsDto{" + "name=" + name + ", count=" + count + '}';
    }
    
    
}
