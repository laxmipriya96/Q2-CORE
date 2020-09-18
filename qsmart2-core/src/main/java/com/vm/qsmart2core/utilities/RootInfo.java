/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.utilities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 *
 * @author Phani
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RootInfo {
    
    private String tableName;
    private int locationId;
    private List<SegmentField> fields;

    public RootInfo() {
    }

    public RootInfo(String tableName, int locationId, List<SegmentField> fields) {
        this.tableName = tableName;
        this.locationId = locationId;
        this.fields = fields;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<SegmentField> getFields() {
        return fields;
    }

    public void setFields(List<SegmentField> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "RootInfo{" + "tableName=" + tableName + ", locationId=" + locationId + ", fields=" + fields + '}';
    }

    
    

}
