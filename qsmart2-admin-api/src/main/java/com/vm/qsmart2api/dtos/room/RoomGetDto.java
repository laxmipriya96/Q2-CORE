/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.room;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class RoomGetDto {
    private List<RoomUpDto> rooms;

    public RoomGetDto() {
    }

    public RoomGetDto(List<RoomUpDto> rooms) {
        this.rooms = rooms;
    }
    
    

    public List<RoomUpDto> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomUpDto> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "RoomGetDto{" + "rooms=" + rooms + '}';
    }

    
    
}
