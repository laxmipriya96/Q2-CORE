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
public class TemplateComponent {
    private String type;
    private List<TemplateParams> parameters;

    public TemplateComponent() {
    }

    public TemplateComponent(String type, List<TemplateParams> parameters) {
        this.type = type;
        this.parameters = parameters;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TemplateParams> getParameters() {
        return parameters;
    }

    public void setParameters(List<TemplateParams> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "TemplateComponent{" + "type=" + type + ", parameters=" + parameters + '}';
    }
    
    
    
}
