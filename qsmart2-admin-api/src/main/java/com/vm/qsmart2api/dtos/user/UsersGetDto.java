/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.user;

import java.util.List;

/**
 *
 * @author Phani
 */
public class UsersGetDto {
    
    private List<UserUDTO> users;

    public UsersGetDto() {
    }

    public UsersGetDto(List<UserUDTO> users) {
        this.users = users;
    }

    public List<UserUDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserUDTO> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "UsersGetDto{" + "users=" + users + '}';
    }
    
    
}
