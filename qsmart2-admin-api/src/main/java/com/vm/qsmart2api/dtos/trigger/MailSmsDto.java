/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.trigger;

import java.util.List;

/**
 *
 * @author Tejasri
 */
public class MailSmsDto {
    private List<String> destinations;
    private String message;   

    public MailSmsDto() {
    }

    public MailSmsDto(List<String> destinations, String message) {
        this.destinations = destinations;
        this.message = message;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

   

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MailSmsDro{" + "destinations=" + destinations + ", message=" + message + '}';
    }
    
    
}
