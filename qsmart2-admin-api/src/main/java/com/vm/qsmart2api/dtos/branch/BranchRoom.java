/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.branch;

import com.vm.qsmart2api.dtos.roomNo.RoomDto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tejasri
 */
public class BranchRoom {
    private Long branchId;
    private String branchNameEn;
    private String branchNameAr;
    
    private List<RoomDto> rooms = new ArrayList<>();

    public BranchRoom() {
    }
    
     public void addRoom(RoomDto room) {
        rooms.add(room);
    }

    public BranchRoom(Long branchId, String branchNameEn, String branchNameAr, List<RoomDto> rooms) {
        this.branchId = branchId;
        this.branchNameEn = branchNameEn;
        this.branchNameAr = branchNameAr;
        this.rooms = rooms;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchNameEn() {
        return branchNameEn;
    }

    public void setBranchNameEn(String branchNameEn) {
        this.branchNameEn = branchNameEn;
    }

    public String getBranchNameAr() {
        return branchNameAr;
    }

    public void setBranchNameAr(String branchNameAr) {
        this.branchNameAr = branchNameAr;
    }

    public List<RoomDto> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDto> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "BranchRoom{" + "branchId=" + branchId + ", branchNameEn=" + branchNameEn + ", branchNameAr=" + branchNameAr + ", rooms=" + rooms + '}';
    }
    
    
}
