/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.displayboard;

/**
 *
 * @author Phani
 */
public class TokensWithRooms {
    
    private long tokenId;
    private String tokenNo;
    private long roomId;
    private String roomNo;
    private String roomMasterEngName;
    private String roomMasterArbName;
    private long timeStamp;

    public TokensWithRooms() {
    }

    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomMasterEngName() {
        return roomMasterEngName;
    }

    public void setRoomMasterEngName(String roomMasterEngName) {
        this.roomMasterEngName = roomMasterEngName;
    }

    public String getRoomMasterArbName() {
        return roomMasterArbName;
    }

    public void setRoomMasterArbName(String roomMasterArbName) {
        this.roomMasterArbName = roomMasterArbName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    @Override
    public String toString() {
        return "TokensWithRooms{" + "tokenId=" + tokenId + ", tokenNo=" + tokenNo + ", roomId=" + roomId + ", roomNo=" + roomNo + ", roomMasterEngName=" + roomMasterEngName + ", roomMasterArbName=" + roomMasterArbName + '}';
    }
    
    
}
