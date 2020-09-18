/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.displayboard;

import java.util.List;

/**
 *
 * @author Phani
 */
public class DisplayBoardTokensDto {
    
    private long displayId;
    private String displayBoardId;
    private String displayName;
    private int themeId;
    private String themeName;
    private String themeType;
    private short isDefault;
    
    private List<TokensWithRooms> tokens;

    public long getDisplayId() {
        return displayId;
    }

    public void setDisplayId(long displayId) {
        this.displayId = displayId;
    }
    
    public DisplayBoardTokensDto() {
    }

    public String getDisplayBoardId() {
        return displayBoardId;
    }

    public void setDisplayBoardId(String displayBoardId) {
        this.displayBoardId = displayBoardId;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
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

    public short getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(short isDefault) {
        this.isDefault = isDefault;
    }

    public List<TokensWithRooms> getTokens() {
        return tokens;
    }

    public void setTokens(List<TokensWithRooms> tokens) {
        this.tokens = tokens;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "DisplayBoardTokensDto{" + "displayBoardId=" + displayBoardId + ", displayName=" + displayName + ", themeId=" + themeId + ", themeName=" + themeName + ", themeType=" + themeType + ", isDefault=" + isDefault + ", tokens=" + tokens + '}';
    }
    
    
        
}
