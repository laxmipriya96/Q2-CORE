/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.menus;

/**
 *
 * @author Tejasri
 */
public class CurrentDateTimeDto {
    
    private String dateTime;
    
    private String date;
    
    private String time;
    
    private String hours;
    
    private String seconds;
    
    private String minutes;
    
    

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        return "CurrentDateTimeDto{" + "dateTime=" + dateTime + ", date=" + date + ", time=" + time + ", hours=" + hours + ", seconds=" + seconds + ", minutes=" + minutes + '}';
    }
    
  
}
