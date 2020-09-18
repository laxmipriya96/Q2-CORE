/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.dtos.role;

/**
 *
 * @author ASHOK
 */
public class RoleDTO {

    private Long roleId;
    private String roleName;
    private String roleCode;
    private int roleType;
    private short isCustomRole;
    private int enterpriseId;
    private int branchType;

    /**
     * @return the roleId
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * @return the roleCode
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * @param roleCode the roleCode to set
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    /**
     * @return the roleType
     */
    

    /**
     * @return the isCustomRole
     */
    public short getIsCustomRole() {
        return isCustomRole;
    }

    /**
     * @param isCustomRole the isCustomRole to set
     */
    public void setIsCustomRole(short isCustomRole) {
        this.isCustomRole = isCustomRole;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public int getBranchType() {
        return branchType;
    }

    /**
     * @return the enterpriseId
     */
    public void setBranchType(int branchType) {    
        this.branchType = branchType;
    }

    @Override
    public String toString() {
        return "RoleDTO{" + "roleId=" + roleId + ", roleName=" + roleName + ", roleCode=" + roleCode + ", roleType=" + roleType + ", isCustomRole=" + isCustomRole + ", enterpriseId=" + enterpriseId + '}';
    }

}
