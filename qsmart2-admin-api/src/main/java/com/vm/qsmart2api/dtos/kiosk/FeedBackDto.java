/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.feedback;

/**
 *
 * @author Ashok
 */
public class FeedBackDto {

    private String mrnNo;
    private String kioskIdentifier;
    private String rating;

    public FeedBackDto() {
    }

    public FeedBackDto(String mrnNo, String kioskIdentifier, String rating) {
        this.mrnNo = mrnNo;
        this.kioskIdentifier = kioskIdentifier;
        this.rating = rating;
    }

    public String getMrnNo() {
        return mrnNo;
    }

    public void setMrnNo(String mrnNo) {
        this.mrnNo = mrnNo;
    }

    public String getKioskIdentifier() {
        return kioskIdentifier;
    }

    public void setKioskIdentifier(String kioskIdentifier) {
        this.kioskIdentifier = kioskIdentifier;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
    
    

}
