/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblMailQueue;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Ashok
 */
public interface MailQueueRepository extends JpaRepository<TblMailQueue, Long> {

    @Query("SELECT tbl FROM TblMailQueue tbl where tbl.status= :status")
    public List<TblMailQueue> getAllPendingMails(@Param("status") int status);

}
