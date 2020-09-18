/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.repositary;

import com.vm.qsmart2.model.TblUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ASHOK
 */
@Repository
public interface UserRepository extends JpaRepository<TblUser, Long> {

    @Query("SELECT tbl FROM TblUser tbl WHERE tbl.resourceCode= :resourceCode")
    public TblUser findUserByResourceCode(@Param("resourceCode") String resourceCode);
    
}
