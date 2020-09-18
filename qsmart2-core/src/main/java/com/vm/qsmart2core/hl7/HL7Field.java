/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.hl7;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author J SRINIVAS
 */
public class HL7Field {

    private String name;
    private String value;
    private String description;
    private boolean child;

    private final List<HL7Field> fields = new ArrayList<>();

    @Override
    public String toString() {
        return "HL7Field{" + "name=" + name + ", value=" + value + ", description=" + description + ", child=" + child + ", fields=" + fields + '}';
    }

    
    
    public void addField(HL7Field field) {
        fields.add(field);
        this.child=true;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the subFields
     */
    public List<HL7Field> getFields() {
        return fields;
    }

    /**
     * @return the child
     */
    public boolean isChild() {
        return !this.fields.isEmpty();
    }

}
