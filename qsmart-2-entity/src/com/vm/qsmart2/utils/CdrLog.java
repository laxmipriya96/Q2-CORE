/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Phani
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CdrLog {
    
    private TokenStatus status;
    private String transId;
    private long locationId;
    private long serviceId;
    private String startTime;
    private String endTime;
    private long roomId;
    private long servedBy;
    private long drId;
    private long branchId;
    private String firstCallTime;
    private long patienId;
    private String checkInTime;
    private String checkOutTime;
    private boolean servingState;

    public boolean isServingState() {
        return servingState;
    }

    public void setServingState(boolean servingState) {
        this.servingState = servingState;
    }

    public TokenStatus getStatus() {
        return status;
    }

    public void setStatus(TokenStatus status) {
        this.status = status;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getServedBy() {
        return servedBy;
    }

    public void setServedBy(long servedBy) {
        this.servedBy = servedBy;
    }

    public long getDrId() {
        return drId;
    }

    public void setDrId(long drId) {
        this.drId = drId;
    }

    public long getBranchId() {
        return branchId;
    }

    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    public String getFirstCallTime() {
        return firstCallTime;
    }

    public void setFirstCallTime(String firstCallTime) {
        this.firstCallTime = firstCallTime;
    }

    public long getPatienId() {
        return patienId;
    }

    public void setPatienId(long patienId) {
        this.patienId = patienId;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    @Override
    public String toString() {
        return "CdrLog{" + "status=" + status + ", transId=" + transId + ", locationId=" + locationId + ", serviceId=" + serviceId + ", startTime=" + startTime + ", endTime=" + endTime + ", roomId=" + roomId + ", servedBy=" + servedBy + ", drId=" + drId + ", branchId=" + branchId + ", firstCallTime=" + firstCallTime + ", patienId=" + patienId + ", checkInTime=" + checkInTime + ", checkOutTime=" + checkOutTime + '}';
    }
    
}
