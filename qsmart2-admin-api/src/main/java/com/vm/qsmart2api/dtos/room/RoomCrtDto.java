/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.room;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 *
 * @author Tejasri
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomCrtDto {
    private String roomNameEn;
    private String roomNameAr;
    private Integer maxAllowedToken;
    //private List<String> roomNos;
   
    public RoomCrtDto() {
    }

    public RoomCrtDto(String roomNameEn, String roomNameAr, Integer maxAllowedToken) {
        this.roomNameEn = roomNameEn;
        this.roomNameAr = roomNameAr;
        this.maxAllowedToken = maxAllowedToken;
    }

    public String getRoomNameEn() {
        return roomNameEn;
    }

    public void setRoomNameEn(String roomNameEn) {
        this.roomNameEn = roomNameEn;
    }

    public String getRoomNameAr() {
        return roomNameAr;
    }

    public void setRoomNameAr(String roomNameAr) {
        this.roomNameAr = roomNameAr;
    }

    public Integer getMaxAllowedToken() {
        return maxAllowedToken;
    }

    public void setMaxAllowedToken(Integer maxAllowedToken) {
        this.maxAllowedToken = maxAllowedToken;
    }

    

    @Override
    public String toString() {
        return "RoomCrtDto{" + "roomNameEn=" + roomNameEn + ", roomNameAr=" + roomNameAr + ", maxAllowedToken=" + maxAllowedToken + '}';
    }
    
    
}
