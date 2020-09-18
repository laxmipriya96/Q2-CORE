/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblNotificationMail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author tejasri
 */
public interface MailNotificationRepository extends JpaRepository<TblNotificationMail, Long>{
    
    @Query("SELECT COUNT(tbl) FROM TblNotificationMail tbl WHERE tbl.tiggerId = :tiggerId and tbl.enterpriseId = :enterpriseId")
    public int validateTriggerId(@Param("tiggerId") Long tiggerId, @Param("enterpriseId") Long enterpriseId);
    
    @Query("SELECT COUNT(tbl) FROM TblNotificationMail tbl WHERE tbl.tiggerId = :tiggerId and tbl.notificationMailId != :notificationMailId and tbl.enterpriseId = :enterpriseId")
    public int validateTriggerIdByMailNotfId(@Param("tiggerId") Long tiggerId,@Param("notificationMailId") Long notificationMailId, @Param("enterpriseId") Long enterpriseId);
    
    @Query("SELECT tbl FROM TblNotificationMail tbl WHERE tbl.enterpriseId = :enterpriseId")
    public List<TblNotificationMail> getAllMailNotificationsByEntId(@Param("enterpriseId") Long enterpriseId);
    
    @Query("SELECT tbl FROM TblNotificationMail tbl "
            + "left join TblTrigger trigger on  trigger.triggerId= tbl.tiggerId "
            + "where tbl.enterpriseId = :enterpriseId and trigger.triggerCode= :triggerType")
    public TblNotificationMail getTemplateText(@Param("enterpriseId") Long enterpriseId, @Param("triggerType") String triggerType);

}
