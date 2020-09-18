/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.kiosk;

import com.vm.qsmart2.utils.DateUtils;
import java.util.Date;

/**
 *
 * @author Ashok
 */
public class KioskAppointmentDTO {

    private String mrnNo;
    private String patientName;
    private String doctorName;
    private String departmentName;
    private String appointmentTime;
    private Long serviceBookedId;

    public KioskAppointmentDTO(String mrnNo, String patientName, String doctorName, String departmentName, Date appointmentTime) {
        this.mrnNo = mrnNo;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.departmentName = departmentName;
        this.appointmentTime = DateUtils.sdf.get().format(appointmentTime);
    }
    
    

    public KioskAppointmentDTO(String mrnNo, String patientName, String doctorName, String departmentName, Date appointmentTime, Long serviceBookedId) {
        this.mrnNo = mrnNo;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.departmentName = departmentName;
        this.appointmentTime = DateUtils.sdf.get().format(appointmentTime);
        this.serviceBookedId = serviceBookedId;
    }
    
    

    public Long getServiceBookedId() {
        return serviceBookedId;
    }

    public void setServiceBookedId(Long serviceBookedId) {
        this.serviceBookedId = serviceBookedId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getMrnNo() {
        return mrnNo;
    }

    public void setMrnNo(String mrnNo) {
        this.mrnNo = mrnNo;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

}
