/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.userprofile;

/**
 *
 * @author Phani
 */
public class UserServingStats {
    
    private int totalAppointments;
    private int checkedInCount;
    private int waitingCount;
    private int averageWaitingTime;
    private int averageCareTime;

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(int totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public int getCheckedInCount() {
        return checkedInCount;
    }

    public void setCheckedInCount(int checkedInCount) {
        this.checkedInCount = checkedInCount;
    }

    public int getWaitingCount() {
        return waitingCount;
    }

    public void setWaitingCount(int waitingCount) {
        this.waitingCount = waitingCount;
    }

    public int getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public void setAverageWaitingTime(int averageWaitingTime) {
        this.averageWaitingTime = averageWaitingTime;
    }

    public int getAverageCareTime() {
        return averageCareTime;
    }

    public void setAverageCareTime(int averageCareTime) {
        this.averageCareTime = averageCareTime;
    }

    @Override
    public String toString() {
        return "UserServingStats{" + "totalAppointments=" + totalAppointments + ", checkedInCount=" + checkedInCount + ", waitingCount=" + waitingCount + ", averageWaitingTime=" + averageWaitingTime + ", averageCoreTime=" + averageCareTime + '}';
    }
    
}
