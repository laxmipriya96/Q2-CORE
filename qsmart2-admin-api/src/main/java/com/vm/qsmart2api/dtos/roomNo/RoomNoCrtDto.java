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
public class RoomNoCrtDto {
    
    private List<String> roomNoa;

    public RoomNoCrtDto() {
    }

    public List<String> getRoomNoa() {
        return roomNoa;
    }

    public void setRoomNoa(List<String> roomNoa) {
        this.roomNoa = roomNoa;
    }

    @Override
    public String toString() {
        return "RoomNoCrtDto{" + "roomNoa=" + roomNoa + '}';
    }

}
