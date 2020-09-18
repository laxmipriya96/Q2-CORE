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
public class RoomNoGDto {
    
    private Long roomId;
    private String roomNo;

    public RoomNoGDto() {
    }

    public RoomNoGDto(Long roomId, String roomNo) {
        this.roomId = roomId;
        this.roomNo = roomNo;
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
        return "RoomGDto{" + "roomId=" + roomId + ", roomNo=" + roomNo + '}';
    }


    
    
}
