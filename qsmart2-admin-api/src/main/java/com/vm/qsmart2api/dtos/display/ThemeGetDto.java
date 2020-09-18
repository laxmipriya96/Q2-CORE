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
public class ThemeGetDto {
    private List<ThemeDto> themes;

    public ThemeGetDto() {
    }

    public ThemeGetDto(List<ThemeDto> themes) {
        this.themes = themes;
    }

    public List<ThemeDto> getThemes() {
        return themes;
    }

    public void setThemes(List<ThemeDto> themes) {
        this.themes = themes;
    }

    @Override
    public String toString() {
        return "ThemeGetDto{" + "themes=" + themes + '}';
    }
    
    
}
