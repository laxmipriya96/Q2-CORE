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
public class HL7Segment {

    private String name;
    private String description;
    private boolean child;
    
    private final List<HL7Field> fields=new ArrayList<>();

    @Override
    public String toString() {
        return "HL7Segment{" + "name=" + name + ", description=" + description + ", child=" + child + ", fields=" + fields + '}';
    }

  

    public void addSegment(HL7Field field) {
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
     * @return the fields
     */
    public List<HL7Field> getFields() {
        return fields;
    }

     /**
     * @return the child
     */
    public boolean isChild() {
        return this.child;
    }
 
//    public void setChild(boolean aFlag) {
//        this.child=aFlag;
//    }

}
