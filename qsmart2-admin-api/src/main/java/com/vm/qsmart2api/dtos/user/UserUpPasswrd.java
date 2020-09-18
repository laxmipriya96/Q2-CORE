/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.user;

/**
 *
 * @author Tejasri
 */
public class UserUpPasswrd {
    
    private String currentPassword;
    private String newPassword;

    public UserUpPasswrd() {
    }

    public UserUpPasswrd(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

   
    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "UserUpPasswrd{" + "currentPassword=" + currentPassword + ", newPassword=" + newPassword + '}';
    }

}
