/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.roomNo;

/**
 *
 * @author Tejasri
 */
public class RoomDto {

    private Long roomId;
    private String roomNo;
    private String roomNameEn;
    private String roomNameAr;
    private Integer maxAllowedToken;

    public RoomDto(Long roomId, String roomNo, String roomNameEn, String roomNameAr) {
        this.roomId = roomId;
        this.roomNo = roomNo;
        this.roomNameEn = roomNameEn;
        this.roomNameAr = roomNameAr;
    }

    public RoomDto(Long roomId, String roomNo, String roomNameEn, String roomNameAr, Integer maxAllowedToken) {
        this.roomId = roomId;
        this.roomNo = roomNo;
        this.roomNameEn = roomNameEn;
        this.roomNameAr = roomNameAr;
        this.maxAllowedToken = maxAllowedToken;
    }

    public RoomDto() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    @Override
    public String toString() {
        return "RoomDto{" + "roomNameEn=" + roomNameEn + ", roomNameAr=" + roomNameAr + ", maxAllowedToken=" + maxAllowedToken + ", roomId=" + roomId + ", roomNo=" + roomNo + '}';
    }
}
