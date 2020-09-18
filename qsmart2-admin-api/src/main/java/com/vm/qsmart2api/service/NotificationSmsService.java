/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblNotificationSms;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.dtos.sms.NotificationSmsDTO;
import com.vm.qsmart2api.dtos.sms.NotificationSmsGDTO;
import com.vm.qsmart2api.dtos.sms.NotificationSmsUDTO;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.NotificationSmsRepository;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ashok
 */
@Service
public class NotificationSmsService {

    public static final Logger logger = LogManager.getLogger(NotificationSmsService.class);

    @Autowired
    NotificationSmsRepository notificationSmsRepository;

    @Autowired
    DateUtils dateUtils;

    public NotificationSmsGDTO getAllNotificationSmsByEnterpriseId(String header, Long userId, Long enterpriseId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:getAllNotificationSmsByEnterpriseId:enterpriseId:[{}]", header, enterpriseId);
            }
            List<TblNotificationSms> notificationSms = notificationSmsRepository.getAllNotificationSmsByEnterpriseId(enterpriseId);
            List<NotificationSmsUDTO> notificationSmsUDTO = Mapper.INSTANCE.notificationSmsListTONotificationSmsUDTO(notificationSms);
            if (logger.isDebugEnabled()) {
                logger.info("{}<<:NotificationSms:[{}]", header, notificationSmsUDTO);
            }
            return new NotificationSmsGDTO(notificationSmsUDTO);
        } catch (Exception e) {
            logger.error("{}Excep:getAllNotificationSmsByEnterpriseId:enterpriseId:{}:Error:{}", header, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            return new NotificationSmsGDTO(new ArrayList<>());
        }
    }

    public long save(String header, Long userId, Long enterpriseId, NotificationSmsDTO notificationSmsDTO) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>save:header:[{}]:enterpriseId:[{}]:notificationSms:[{}]", header, enterpriseId, notificationSmsDTO);
            }
            TblNotificationSms notificationSms = Mapper.INSTANCE.notifiDtoTONotifiEntity(notificationSmsDTO);
            notificationSms.setEnterpriseId(enterpriseId);
            notificationSms.setCreatedBy(userId);
            notificationSms.setCreatedOn(dateUtils.getdate());
            notificationSms.setUpdatedBy(userId);
            notificationSms.setUpdatedOn(dateUtils.getdate());
            logger.info("<<:header:[{}]:notificationSms:[{}]", header, notificationSmsDTO);
            return notificationSmsRepository.saveAndFlush(notificationSms).getNotificationSmsId();

        } catch (Exception e) {
            logger.error("{}Excep:save:header:{}:enterpriseId:{}:Error:{}", header, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }
    
        public long update(String header, Long userId, Long enterpriseId, NotificationSmsUDTO notificationSmsUDTO) {
       try {
            if (logger.isDebugEnabled()) {
                logger.info(">>update:header:[{}]:enterpriseId:[{}]:notificationSms:[{}]", header, enterpriseId, notificationSmsUDTO);
            }
            TblNotificationSms notificationSms = Mapper.INSTANCE.notifiUDtoTONotifiEntity(notificationSmsUDTO);
            notificationSms.setEnterpriseId(enterpriseId);
            notificationSms.setUpdatedBy(userId);
            notificationSms.setUpdatedOn(dateUtils.getdate());
            logger.info("<<:header:[{}]:notificationSms:[{}]", header, notificationSmsUDTO);
            return notificationSmsRepository.saveAndFlush(notificationSms).getNotificationSmsId();

        } catch (Exception e) {
            logger.error("{}Excep:update:header:{}:enterpriseId:{}:Error:{}", header, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

    public boolean validateNotificationSmsWithTriggerId(String header, Long enterpriseId, Long triggerId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>validateNotificationSmsWithTriggerId:header:[{}]:enterpriseId:[{}]:triggerId:[{}]", header, enterpriseId, triggerId);
            }
            int count = notificationSmsRepository.countByEnterpriseIdAndTriggerId(enterpriseId, triggerId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            logger.error("{}Excep:validateNotificationSmsWithTriggerId:header:{}:enterpriseId:{}:Error:{}", header, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }

    }



    public boolean validateNotificationSmsWithTriggerIdByNotificationId(String header, Long enterpriseId, Long notificationSmsId, Long triggerId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info(">>validateNotificationSmsWithTriggerIdByNotificationId:header:[{}]:enterpriseId:[{}]:triggerId:[{}]", header, enterpriseId, triggerId);
            }
            int count = notificationSmsRepository.countByEnterpriseIdAndTriggerIdAndNotifiSmsId(enterpriseId,notificationSmsId, triggerId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            logger.error("{}Excep:validateNotificationSmsWithTriggerIdByNotificationId:header:{}:enterpriseId:{}:Error:{}", header, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }
}
