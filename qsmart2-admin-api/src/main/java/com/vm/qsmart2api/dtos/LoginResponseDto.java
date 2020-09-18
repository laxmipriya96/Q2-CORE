/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author VMHDCLAP21
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponseDto extends Response {
    
    public UserDetails userDetails;
    

    public LoginResponseDto() {
    }


    public LoginResponseDto( boolean status, String messages) {
        super(status, messages);
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public String toString() {
        return "LoginResponseDto{" + "userDetails=" + userDetails + '}';
    }

 
  }
