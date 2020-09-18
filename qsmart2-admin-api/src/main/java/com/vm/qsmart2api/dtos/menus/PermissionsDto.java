/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.menus;

/**
 *
 * @author Phani
 */
public class PermissionsDto {
    
    private String permission;
    private int permissionId;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public String toString() {
        return "PermissionsDto{" + "permission=" + permission + ", permissionId=" + permissionId + '}';
    }

   
   
}
