/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.activedirectory;

import java.util.List;

/**
 *
 * @author VMHDCLAP21
 */
public class DirectoryGDto{
    
    private boolean status;
    private String message;

    private List<DirectoryUsersDto> users;

    public DirectoryGDto() {
    }

    public DirectoryGDto(List<DirectoryUsersDto> users) {
        this.users = users;
    }

    public DirectoryGDto(boolean status, String message) {
        this.status = status;
        this.message = message;
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
    
    public DirectoryGDto(boolean status, String message, List<DirectoryUsersDto> users) {
        this.status = status;
        this.message = message;
        this.users = users;
    }
    
    

    public List<DirectoryUsersDto> getUsers() {
        return users;
    }

    public void setUsers(List<DirectoryUsersDto> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "DirectoryGDto{" + "users=" + users + '}';
    }

}
