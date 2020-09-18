/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.display;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class DisplayResponse {
    private List<DisplayUpDto> displays;// = new ArrayList<>();

    public DisplayResponse() {
    }

    public List<DisplayUpDto> getDisplays() {
        return displays;
    }

    public void setDisplays(List<DisplayUpDto> displays) {
        this.displays = displays;
    }

    public DisplayResponse(List<DisplayUpDto> displays) {
        this.displays = displays;
    }

    @Override
    public String toString() {
        return "DisplayResponse{" + "displays=" + displays + '}';
    }

    
    
}
