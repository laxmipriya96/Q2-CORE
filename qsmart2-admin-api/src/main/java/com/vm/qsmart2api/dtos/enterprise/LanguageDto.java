/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.enterprise;

/**
 *
 * @author Tejasri
 */
public class LanguageDto {
    
    private Long id;
    private String langCode;
    private String language;

    public LanguageDto() {
    }

    public LanguageDto(Long id, String langCode, String language) {
        this.id = id;
        this.langCode = langCode;
        this.language = language;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "LanguageDto{" + "id=" + id + ", langCode=" + langCode + ", language=" + language + '}';
    }
}
