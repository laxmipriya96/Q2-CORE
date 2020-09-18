/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.user;

/**
 *
 * @author tejasri
 */
public class ProfileUpDto {
    private String firstName;
    private String lastName;
    private String emailId;
    private String contactNo;

    public ProfileUpDto() {
    }

    public ProfileUpDto(String firstName, String lastName, String emailId, String contactNo) {
//        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.contactNo = contactNo;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    @Override
    public String toString() {
        return "ProfileUpDto{" + "firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId + ", contactNo=" + contactNo + '}';
    }

}
