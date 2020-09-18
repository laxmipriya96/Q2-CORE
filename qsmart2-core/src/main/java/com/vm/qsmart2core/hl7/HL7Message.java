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
public class HL7Message {

    private String name;
    private String version;
    private String description;

    private boolean child = false;
    private List<HL7Segment> segments = new ArrayList<>();


    @Override
    public String toString() {
        return "HL7Message{" + "name=" + getName() + ", version=" + getVersion() + ", description=" + getDescription() + ", child=" + isChild() + ", segments=" + getSegments() + '}';
    }

    public void addSegment(HL7Segment segment) {
        segments.add(segment);
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
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
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
     * @return the child
     */
    public boolean isChild() {
        return !this.segments.isEmpty();
    }

    /**
     * @return the segments
     */
    public List<HL7Segment> getSegments() {
        return segments;
    }

}
