/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.repository;

import com.vm.qsmart2.model.TblNotificationSms;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Ashok
 */
public interface NotificationSmsRepository extends JpaRepository<TblNotificationSms, Long> {

    @Query("SELECT tbl FROM TblNotificationSms tbl  where tbl.enterpriseId = :enterpriseId order by tbl.notificationSmsId DESC")
    public List<TblNotificationSms> getAllNotificationSmsByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    @Query("SELECT COUNT(tbl) FROM TblNotificationSms tbl  where tbl.enterpriseId = :enterpriseId and tbl.triggerId= :triggerId")
    public int countByEnterpriseIdAndTriggerId(@Param("enterpriseId") Long enterpriseId, @Param("triggerId") Long triggerId);

    @Query("SELECT COUNT(tbl) FROM TblNotificationSms tbl  where tbl.enterpriseId = :enterpriseId and tbl.triggerId= :triggerId and tbl.notificationSmsId != :notificationSmsId")
    public int countByEnterpriseIdAndTriggerIdAndNotifiSmsId(@Param("enterpriseId") Long enterpriseId, @Param("notificationSmsId") Long notificationSmsId, @Param("triggerId") Long triggerId);

    @Query("SELECT tbl.smsTxt FROM TblNotificationSms tbl "
            + "left join TblTrigger trigger on  trigger.triggerId= tbl.triggerId "
            + "where tbl.enterpriseId = :enterpriseId and trigger.triggerCode= :triggerType")
    public String getTemplateText(@Param("enterpriseId") Long enterpriseId, @Param("triggerType") String triggerType);

}
