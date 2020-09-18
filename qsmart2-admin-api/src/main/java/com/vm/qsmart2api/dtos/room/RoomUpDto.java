/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.room;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 *
 * @author Tejasri
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomUpDto {

    private Long roomMasterId;
    private String roomNameEn;
    private String roomNameAr;
    private Integer maxAllowedToken;

   // private List<String> roomNos = new ArrayList<>();

    public RoomUpDto() {
    }

//    public RoomUpDto(Long roomMasterId, String roomNameEn, String roomNameAr, Integer maxAllowedToken, List<String> roomNos) {
//        this.roomMasterId = roomMasterId;
//        this.roomNameEn = roomNameEn;
//        this.roomNameAr = roomNameAr;
//        this.maxAllowedToken = maxAllowedToken;
//        this.roomNos = roomNos;
//    }

    public Long getRoomMasterId() {
        return roomMasterId;
    }

    public void setRoomMasterId(Long roomMasterId) {
        this.roomMasterId = roomMasterId;
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
        return "RoomUpDto{" + "roomMasterId=" + roomMasterId + ", roomNameEn=" + roomNameEn + ", roomNameAr=" + roomNameAr + ", maxAllowedToken=" + maxAllowedToken + '}';
    }

    
}
