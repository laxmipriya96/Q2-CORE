/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblService;
import com.vm.qsmart2.model.TblUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ASHOK
 */
@Repository
public interface UserRepository extends JpaRepository<TblUser, Long> {

    @Query("SELECT COUNT(tbl) FROM TblUser tbl WHERE UPPER(tbl.userName)= :userName")
    public int validateUserNameInDb(@Param("userName") String userName);

    @Query("SELECT COUNT(tbl) FROM TblUser tbl WHERE UPPER(tbl.userName)= :userName and tbl.userId != :userId")
    public int validateUserNameInDbByUserId(@Param("userName") String userName, @Param("userId") long userId);

    @Query("SELECT COUNT(tbl) FROM TblUser tbl WHERE tbl.emailId= :emailId")
    public int validateEmailInDb(@Param("emailId") String emailId);

    @Query("SELECT distinct tbl FROM TblUser tbl "
            + "left join fetch tbl.roles roles "
            + "left join fetch tbl.enterprise enterprise "
            + "WHERE enterprise.enterpriseId= :enterpriseId and (roles.roleId != :roleId or roles.roleId is Null) ORDER BY tbl.userId DESC")
    public List<TblUser> getUsersByRoleCodeAndEnterpriseId(@Param("enterpriseId") long enterpriseId, @Param("roleId") long roleId);

    @Query("SELECT distinct tbl FROM TblUser tbl join fetch tbl.roles roles WHERE roles.roleCode = :roleCode ORDER BY tbl.userId DESC")
    public List<TblUser> getUsersByRoleCode(@Param("roleCode") String roleCode);

    @Query("SELECT distinct tbl FROM TblUser tbl left join fetch tbl.roles roles left join fetch tbl.enterprise left join fetch tbl.locations WHERE tbl.userName = :userName")
    public TblUser getUsersByUsername(@Param("userName") String username);

    @Query("SELECT distinct tbl FROM TblUser tbl "
            + "left join fetch tbl.roles roles "
            + "left join fetch tbl.locations location "
            + "left join fetch tbl.enterprise enterprise "
            + " WHERE enterprise.enterpriseId= :enterpriseId and location.locationId= :locationId and tbl.userId != :userId and roles.roleCode != :roleCode1 ORDER BY tbl.userId DESC")
    public List<TblUser> getUsersByRoleCodeAndLocationId(@Param("roleCode") String roleCode, @Param("roleCode1") String roleCode1, @Param("enterpriseId") long enterpriseId, @Param("locationId") long locationId, @Param("userId") long userId);

    @Query("SELECT distinct tbl FROM TblUser tbl "
            + "left join fetch tbl.roles roles "
            + "left join fetch tbl.locations location "
            + " WHERE location.locationId= :locationId and roles.roleCode= :roleCode  ORDER BY tbl.userId DESC")
    public List<TblUser> getUsersByRoleCodeAndAndLocationId(@Param("locationId") long locationId, @Param("roleCode") String roleCode);

    
    @Query("SELECT distinct tbl FROM TblUser tbl "
            + "left join fetch tbl.roles roles "
            + "left join fetch tbl.enterprise enterprise "
            + "left join fetch tbl.locations location "
            + "left join fetch tbl.medServices medServices "
            + " WHERE location.locationId= :locationId and roles.roleCode= :roleCode and medServices is Null ORDER BY tbl.userId DESC")
    public List<TblUser> getNonAssignedDrsByRoleCodeAndAndLocationId(@Param("locationId") long locationId, @Param("roleCode") String roleCode);

    @Modifying
    @Query("UPDATE TblUser tbl  set tbl.hashPassword= :newPassword  WHERE tbl.userId = :userId")
    public int updatePassword(@Param("userId") long userId,@Param("newPassword") String newPassword);
    
    @Modifying
    @Query("UPDATE TblUser tbl  set tbl.firstName= :firstName, tbl.lastName = :lastName, tbl.emailId = :emailId, "
            + "tbl.contactNo = :contactNo  WHERE tbl.userId = :userId")
    public int updateProfile(@Param("userId") long userId, 
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("emailId") String emailId,
            @Param("contactNo") String contactNo);
    
    
    @Modifying
    @Transactional
    @Query("delete from TblUserProfile tbl WHERE tbl.userId =:userId")
    public void deleteByUserIdInDb(@Param("userId") long userId);
    
    
    @Query("SELECT distinct tu from TblUser tu "
            + "join fetch tu.enterprise te "
            + "join fetch tu.roles tr "
            + "join fetch tu.apptTypes tat "
            + "join fetch tat.services ts "
            + "where tr.roleCode = :roleCode and ts.serviceId = :serviceId")
    public List<TblUser> getAllDoctorsByServiceId(@Param("roleCode") String roleCode, @Param("serviceId") long serviceId);

}
