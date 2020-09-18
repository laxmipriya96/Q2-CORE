/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.token;

/**
 *
 * @author Ashok
 */
public class TokenUDTO {

    private Long tokenId;
    private Integer roomNo;
    private String startTime;
    private String endTime;
    private Integer firstCall;
    private Integer noShow;
    private Long servedBy;

    /**
     * @return the tokenId
     */
    public Long getTokenId() {
        return tokenId;
    }

    /**
     * @param tokenId the tokenId to set
     */
    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }
    /**
     * @return the roomNo
     */
    public Integer getRoomNo() {
        return roomNo;
    }

    /**
     * @param roomNo the roomNo to set
     */
    public void setRoomNo(Integer roomNo) {
        this.roomNo = roomNo;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the firstCall
     */
    public Integer getFirstCall() {
        return firstCall;
    }

    /**
     * @param firstCall the firstCall to set
     */
    public void setFirstCall(Integer firstCall) {
        this.firstCall = firstCall;
    }

    /**
     * @return the noShow
     */
    public Integer getNoShow() {
        return noShow;
    }

    /**
     * @param noShow the noShow to set
     */
    public void setNoShow(Integer noShow) {
        this.noShow = noShow;
    }

    /**
     * @return the servedBy
     */
    public Long getServedBy() {
        return servedBy;
    }

    /**
     * @param servedBy the servedBy to set
     */
    public void setServedBy(Long servedBy) {
        this.servedBy = servedBy;
    }

}
