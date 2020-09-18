/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.enterprise;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class LanguageGetDto {
            private List<LanguageDto> languages;

    public LanguageGetDto() {
    }

    public LanguageGetDto(List<LanguageDto> languages) {
        this.languages = languages;
    }

    public List<LanguageDto> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LanguageDto> languages) {
        this.languages = languages;
    }

    @Override
    public String toString() {
        return "LanguageGetDto{" + "languages=" + languages + '}';
    }
    
}
