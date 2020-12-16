/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 *
 * @author Phani
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateNotification {
    
    private String name;
    private String language;
    private List<TemplateComponent> components;

    public TemplateNotification() {
    }

    public TemplateNotification(String name, String language, List<TemplateComponent> components) {
        this.name = name;
        this.language = language;
        this.components = components;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<TemplateComponent> getComponents() {
        return components;
    }

    public void setComponents(List<TemplateComponent> components) {
        this.components = components;
    }

    @Override
    public String toString() {
        return "TemplateNotification{" + "name=" + name + ", language=" + language + ", components=" + components + '}';
    }
    
    
    
}
