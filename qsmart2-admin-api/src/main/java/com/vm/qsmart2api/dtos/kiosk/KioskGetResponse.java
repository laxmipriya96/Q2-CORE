/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.kiosk;

import java.util.List;

/**
 *
 * @author Phani
 */
public class KioskGetResponse {

    private List<KioskUpDto> kiosks;

    public KioskGetResponse() {
    }

    public KioskGetResponse(List<KioskUpDto> kiosks) {
        this.kiosks = kiosks;
    }
    

    public List<KioskUpDto> getKiosks() {
        return kiosks;
    }

    public void setKiosks(List<KioskUpDto> kiosks) {
        this.kiosks = kiosks;
    }

    @Override
    public String toString() {
        return "KioskGetResponse{" + "kiosks=" + kiosks + '}';
    }
    
    
}
