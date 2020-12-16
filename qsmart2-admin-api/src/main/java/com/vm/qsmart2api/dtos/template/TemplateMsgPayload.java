/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.template;

/**
 *
 * @author Phani
 */
public class TemplateMsgPayload {
    
    private String type;
    private TemplatePayload template;

    public TemplateMsgPayload() {
    }

    public TemplateMsgPayload(String type, TemplatePayload template) {
        this.type = type;
        this.template = template;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TemplatePayload getTemplate() {
        return template;
    }

    public void setTemplate(TemplatePayload template) {
        this.template = template;
    }

    @Override
    public String toString() {
        return "TemplateMsgPayload{" + "type=" + type + ", template=" + template + '}';
    }
    
}
