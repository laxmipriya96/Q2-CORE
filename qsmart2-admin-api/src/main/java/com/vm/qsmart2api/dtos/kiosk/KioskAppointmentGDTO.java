/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.kiosk;

import java.util.List;

/**
 *
 * @author Ashok
 */
public class KioskAppointmentGDTO {

    private boolean status;
    private String message;
    private List<KioskAppointmentDTO> kioskAppointmentdto;

    public KioskAppointmentGDTO() {
    }

    public KioskAppointmentGDTO(boolean status, String message, List<KioskAppointmentDTO> kioskAppointmentdto) {
        this.status = status;
        this.message = message;
        this.kioskAppointmentdto = kioskAppointmentdto;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
    
    public KioskAppointmentGDTO(List<KioskAppointmentDTO> kioskAppointmentdto) {
        this.kioskAppointmentdto = kioskAppointmentdto;
    }

    public List<KioskAppointmentDTO> getKioskAppointmentdto() {
        return kioskAppointmentdto;
    }

    public void setKioskAppointmentdto(List<KioskAppointmentDTO> kioskAppointmentdto) {
        this.kioskAppointmentdto = kioskAppointmentdto;
    }

}
