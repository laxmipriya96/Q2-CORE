/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.roomNo;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class RoomNoGetGDto {
    
  private List<RoomNoGDto> roomNos;

    public RoomNoGetGDto() {
    }

    public RoomNoGetGDto(List<RoomNoGDto> roomNos) {
        this.roomNos = roomNos;
    }
    

    public List<RoomNoGDto> getRoomNos() {
        return roomNos;
    }

    public void setRoomNos(List<RoomNoGDto> roomNos) {
        this.roomNos = roomNos;
    }

    @Override
    public String toString() {
        return "RoomGetGDto{" + "roomNos=" + roomNos + '}';
    }
  
}
