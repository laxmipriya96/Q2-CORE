/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.display;

/**
 *
 * @author Tejasri
 */
public class ThemeDto {
    private Long themeId;
    private String themeName;
    private String themeType;

    public ThemeDto() {
    }

    public ThemeDto(Long themeId, String themeName, String themeType) {
        this.themeId = themeId;
        this.themeName = themeName;
        this.themeType = themeType;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeType() {
        return themeType;
    }

    public void setThemeType(String themeType) {
        this.themeType = themeType;
    }

    @Override
    public String toString() {
        return "ThemeDto{" + "themeId=" + themeId + ", themeName=" + themeName + ", themeType=" + themeType + '}';
    }
}
