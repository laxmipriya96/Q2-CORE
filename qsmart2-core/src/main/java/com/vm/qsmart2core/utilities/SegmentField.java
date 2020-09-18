/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.utilities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Phani
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SegmentField {
    
    private String columnName;
    private String dataType;
    private boolean isHl7Segment;
    private String defaultValue;
    private String expectedFormat;
    private String segmentId;
    private boolean isRepeatable;
    private String repeatSegment;
    private boolean sameSegment;
    private boolean isConditional;
    private String conditionalSegmentId;
    private String conditionalSegmentValue;

    public SegmentField() {
    }

    public SegmentField(String columnName, String dataType, boolean isHl7Segment, String defaultValue, String segmentId, boolean isRepeatable, boolean isConditional, String conditionalSegmentId, String conditionalSegmentValue) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.isHl7Segment = isHl7Segment;
        this.defaultValue = defaultValue;
        this.segmentId = segmentId;
        this.isRepeatable = isRepeatable;
        this.isConditional = isConditional;
        this.conditionalSegmentId = conditionalSegmentId;
        this.conditionalSegmentValue = conditionalSegmentValue;
    }

    public boolean isSameSegment() {
        return sameSegment;
    }

    public void setSameSegment(boolean sameSegment) {
        this.sameSegment = sameSegment;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isIsHl7Segment() {
        return isHl7Segment;
    }

    public void setIsHl7Segment(boolean isHl7Segment) {
        this.isHl7Segment = isHl7Segment;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getExpectedFormat() {
        return expectedFormat;
    }

    public void setExpectedFormat(String expectedFormat) {
        this.expectedFormat = expectedFormat;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public boolean isIsRepeatable() {
        return isRepeatable;
    }

    public void setIsRepeatable(boolean isRepeatable) {
        this.isRepeatable = isRepeatable;
    }

    public String getRepeatSegment() {
        return repeatSegment;
    }

    public void setRepeatSegment(String repeatSegment) {
        this.repeatSegment = repeatSegment;
    }

    public boolean isIsConditional() {
        return isConditional;
    }

    public void setIsConditional(boolean isConditional) {
        this.isConditional = isConditional;
    }

    public String getConditionalSegmentId() {
        return conditionalSegmentId;
    }

    public void setConditionalSegmentId(String conditionalSegmentId) {
        this.conditionalSegmentId = conditionalSegmentId;
    }

    public String getConditionalSegmentValue() {
        return conditionalSegmentValue;
    }

    public void setConditionalSegmentValue(String conditionalSegmentValue) {
        this.conditionalSegmentValue = conditionalSegmentValue;
    }

    

    @Override
    public String toString() {
        return "SegmentField{" + "columnName=" + columnName + ", dataType=" + dataType + ", isHl7Segment=" + isHl7Segment + ", defaultValue=" + defaultValue + ", segmentId=" + segmentId + ", isRepeatable=" + isRepeatable + ", repeatSegment=" + repeatSegment + ", isConditional=" + isConditional + ", conditionalSegmentId=" + conditionalSegmentId + ", conditionalSegmentValue=" + conditionalSegmentValue + '}';
    }

}
