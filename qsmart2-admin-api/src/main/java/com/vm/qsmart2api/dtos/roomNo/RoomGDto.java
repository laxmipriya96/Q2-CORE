/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.roomNo;

import java.util.List;

/**
 *
 * @author Ashok
 */
public class RoomGDto {

    List<RoomDto> rooms;

    public RoomGDto() {
    }
    

    public RoomGDto(List<RoomDto> rooms) {
        this.rooms = rooms;
    }

    public List<RoomDto> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDto> rooms) {
        this.rooms = rooms;
    }

}
