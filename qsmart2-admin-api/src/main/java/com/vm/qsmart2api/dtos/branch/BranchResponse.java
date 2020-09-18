/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.branch;

import com.vm.qsmart2api.dtos.location.BranchGetDto;
import java.util.List;

/**
 *
 * @author Tejasri
 */
public class BranchResponse {
    private String name;
    private List<BranchGetDto> children;

    public BranchResponse() {
    }

    public BranchResponse(String name, List<BranchGetDto> children) {
        this.name = name;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BranchGetDto> getChildren() {
        return children;
    }

    public void setChildren(List<BranchGetDto> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "BranchResponse{" + "name=" + name + ", children=" + children + '}';
    }
}
