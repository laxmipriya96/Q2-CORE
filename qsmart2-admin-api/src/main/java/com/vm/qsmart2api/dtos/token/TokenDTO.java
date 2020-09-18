/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.token;

import com.vm.qsmart2.utils.DateUtils;
import java.util.Date;

/**
 *
 * @author Ashok
 */
public class TokenDTO {

    private Long tokenId;
    private String tokenNo;
    private String mrnNo;
    private String patientName;
    private long drId = 0;
    private String doctorName = "";
    private long roomId;
    private String roomNumber;
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String appointmentTime;
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String checkInTime;
    private String updatedOn;
    private int WaitingTime = 5;
    private boolean isVitalDone;
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String servingCallTime;
    private short priority = 0;
    private String tranId;

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public short getPriority() {
        return priority;
    }

    public void setPriority(short priority) {
        this.priority = priority;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn  = DateUtils.sdf.get().format(updatedOn);
    }
    
    public TokenDTO() {
    }

    public TokenDTO(Long tokenId, String tokenNo, String mrnNo, String patientName, String doctorName, Date appointmentTime, Date CheckInTime, Long drId, short priority, String tranId, Date updatedOn) {
        this.tokenId = tokenId;
        this.tokenNo = tokenNo;
        this.mrnNo = mrnNo;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentTime = DateUtils.sdf.get().format(appointmentTime);
        this.checkInTime = DateUtils.sdf.get().format(CheckInTime);
        this.updatedOn =  DateUtils.sdf.get().format(updatedOn);
        this.WaitingTime = caluclateWaitTime(updatedOn, new Date());
        this.drId = (drId != null )? drId : 0l;
        this.priority = priority;
        this.tranId = tranId;
    }
    
    public final int caluclateWaitTime(Date date1, Date date2) {
        long diff = date2.getTime() - date1.getTime();
        int diffmin = (int) (diff / (60 * 1000));
        return diffmin;
    }
    
    public TokenDTO(Long tokenId, String tokenNo, String mrnNo, String patientName, Date appointmentTime, Date CheckInTime, short priority, String tranId, Date updatedOn) {
        this.tokenId = tokenId;
        this.tokenNo = tokenNo;
        this.mrnNo = mrnNo;
        this.patientName = patientName;
        this.doctorName = "NA";
        this.appointmentTime = DateUtils.sdf.get().format(appointmentTime);
        this.checkInTime = DateUtils.sdf.get().format(CheckInTime);
        this.updatedOn =  DateUtils.sdf.get().format(updatedOn);
        this.WaitingTime = caluclateWaitTime(updatedOn, new Date());
        this.drId = 0;
        this.priority = priority;
        this.tranId = tranId;
    }

    public TokenDTO(Long tokenId, String tokenNo, String mrnNo, String patientName, String doctorName, Date appointmentTime, Date CheckInTime, Long drId, Date servingCallTime, long roomId, String roomNo, short priority, String tranId, Date updatedOn) {
        this.tokenId = tokenId;
        this.tokenNo = tokenNo;
        this.mrnNo = mrnNo;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentTime = DateUtils.sdf.get().format(appointmentTime);
        this.checkInTime = DateUtils.sdf.get().format(CheckInTime);
        this.drId = (drId != null )? drId : 0l;
        this.servingCallTime = DateUtils.sdf.get().format(servingCallTime);
        this.updatedOn =  DateUtils.sdf.get().format(updatedOn);
        this.WaitingTime = caluclateWaitTime(updatedOn, new Date());
        this.roomId = roomId;
        this.roomNumber = roomNo;
        this.priority = priority;
        this.tranId = tranId;
    }
    
    public TokenDTO(Long tokenId, String tokenNo, String mrnNo, String patientName, Date appointmentTime, Date CheckInTime, Date servingCallTime, long roomId, String roomNo, short priority, String tranId, Date updatedOn) {
        this.tokenId = tokenId;
        this.tokenNo = tokenNo;
        this.mrnNo = mrnNo;
        this.patientName = patientName;
        this.appointmentTime = DateUtils.sdf.get().format(appointmentTime);
        this.checkInTime = DateUtils.sdf.get().format(CheckInTime);
        this.servingCallTime = DateUtils.sdf.get().format(servingCallTime);
        this.updatedOn =  DateUtils.sdf.get().format(updatedOn);
        this.WaitingTime = caluclateWaitTime(updatedOn, new Date());
        this.roomId = roomId;
        this.roomNumber = roomNo;
        this.priority = priority;
        this.tranId = tranId;
    }

    public String getServingCallTime() {
        return servingCallTime;
    }

    public void setServingCallTime(Date servingCallTime) {
        this.servingCallTime = DateUtils.sdf.get().format(servingCallTime);
    }

    public boolean isIsVitalDone() {
        return isVitalDone;
    }

    public void setIsVitalDone(boolean isVitalDone) {
        this.isVitalDone = isVitalDone;
    }

    public long getDrId() {
        return drId;
    }

    public void setDrId(long drId) {
        this.drId = drId;
    }

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
     * @return the tokenNo
     */
    public String getTokenNo() {
        return tokenNo;
    }

    /**
     * @param tokenNo the tokenNo to set
     */
    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    /**
     * @return the mrnNo
     */
    public String getMrnNo() {
        return mrnNo;
    }

    /**
     * @param mrnNo the mrnNo to set
     */
    public void setMrnNo(String mrnNo) {
        this.mrnNo = mrnNo;
    }

    /**
     * @return the patientName
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * @param patientName the patientName to set
     */
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    /**
     * @return the doctorName
     */
    public String getDoctorName() {
        return doctorName;
    }

    /**
     * @param doctorName the doctorName to set
     */
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    /**
     * @return the appointmentTime
     */
    public String getAppointmentTime() {
        return appointmentTime;
    }

    /**
     * @param appointmentTime the appointmentTime to set
     */
    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = DateUtils.sdf.get().format(appointmentTime);
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = DateUtils.sdf.get().format(checkInTime);
    }

    /**
     * @return the WaitingTime
     */
    public int getWaitingTime() {
        return WaitingTime;
    }

    /**
     * @param WaitingTime the WaitingTime to set
     */
    public void setWaitingTime(int WaitingTime) {
        this.WaitingTime = WaitingTime;
    }

}
