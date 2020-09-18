/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblNotificationMail;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.dtos.mailnotification.MailCrtDto;
import com.vm.qsmart2api.dtos.mailnotification.MailGetDto;
import com.vm.qsmart2api.dtos.mailnotification.MailUpDto;
import com.vm.qsmart2api.mapper.DevicesMapper;
import com.vm.qsmart2api.mapper.Mapper;
import com.vm.qsmart2api.repository.MailNotificationRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.persistence.NoResultException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tejasri
 */
@Service
public class MailNotificationService {

    public static final Logger logger = LogManager.getLogger(MailNotificationService.class);

    @Autowired
    MailNotificationRepository mailNotificationRepo;

    @Autowired
    DateUtils dateUtils;

    public long saveMailNotificationInDb(String header, Long userId, MailCrtDto mail, Long enterpriseId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>save:enterpriseId:{}:mail:[{}]", header, enterpriseId, mail);
            }
            TblNotificationMail mailTbl = Mapper.INSTANCE.mailNotificationCrtDtoToEntityMail(mail);
            mailTbl.setEnterpriseId(enterpriseId);
            mailTbl.setCreatedBy(userId);
            mailTbl.setCreatedOn(dateUtils.getdate());
            mailTbl.setUpdatedOn(dateUtils.getdate());
            mailTbl.setUpdatedBy(userId);

            mailTbl = mailNotificationRepo.saveAndFlush(mailTbl);
            if (logger.isDebugEnabled()) {
                logger.info("{}<<:mail:[{}]", header, mailTbl.getNotificationMailId());
            }
            return mailTbl.getNotificationMailId();
        } catch (Exception e) {
            logger.error("{}Excep:saveMailNotificationInDb:enterpriseId:{}:mail:{}:Error:{}", header, enterpriseId, mail, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        }
    }

    public boolean validateTriggerId(String header, Long enterpriseId, Long tiggerId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>header:{}:enterpriseId:{}:triggerId:{}", header, enterpriseId, tiggerId);
            }
            int count = mailNotificationRepo.validateTriggerId(tiggerId, enterpriseId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateTriggerId:enterpriseId:{}:triggerId:{},Error:{}", header, enterpriseId, tiggerId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateTriggerId:enterpriseId:{}:triggerId:{},Error:{}", header, enterpriseId, tiggerId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public boolean validateTriggerIdByMailNotfId(String header, Long enterpriseId, Long notificationMailId, Long tiggerId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>header:{},enterpriseId:{}:notificationMailId:{}:triggerId:{}", header, enterpriseId, notificationMailId, tiggerId);
            }
            int count = mailNotificationRepo.validateTriggerIdByMailNotfId(tiggerId, notificationMailId, enterpriseId);
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException ne) {
            logger.error("{}Excep:validateTriggerIdByMailNotfId:enterpriseId:{}:notificationMailId:{}:triggerId:{},Error:{}", header, enterpriseId, notificationMailId, tiggerId, ExceptionUtils.getRootCauseMessage(ne));
            return false;
        } catch (Exception e) {
            logger.error("{}Excep:validateTriggerIdByMailNotfId:enterpriseId:{}:notificationMailId:{}:triggerId:{},Error:{}", header, enterpriseId, notificationMailId, tiggerId, ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }

    public long updateMailNotificationInDb(String header, Long userId, long enterpriseId, MailUpDto mailUp) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>userId:{}:enterpriseId:{}:MailUpDto:[{}]", header, userId, enterpriseId, mailUp);
            }
            TblNotificationMail mailTbl = Mapper.INSTANCE.mailUpDtoToNotificationMailEntity(mailUp);
            mailTbl.setEnterpriseId(enterpriseId);
            mailTbl.setUpdatedBy(userId);
            mailTbl.setUpdatedOn(dateUtils.getdate());

            logger.info("{}<<header:[{}]:MailUpDto:[{}]", header, mailUp);
            mailNotificationRepo.saveAndFlush(mailTbl);
            return mailTbl.getNotificationMailId();
        } catch (Exception e) {
            logger.error("{}Excep:updateMailNotificationInDb:userId:{}:enterpriseId:{}:MailUpDto:[{}]:Error:{}", header, userId, enterpriseId, mailUp, ExceptionUtils.getRootCauseMessage(e));
            return 0;
        } finally {
            mailUp = null;
        }
    }

    public MailGetDto getAllMailNotificationsByEntId(String header, Long userId, Long enterpriseId) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}>>:getAllMailNotificationsByEntId:enterpriseId:[{}]", header, enterpriseId);
            }
            List<TblNotificationMail> notificationList = mailNotificationRepo.getAllMailNotificationsByEntId(enterpriseId);
            List<MailUpDto> mailNotifcations = Mapper.INSTANCE.listOfEntityNotificationMailToMailsList(notificationList);

            if (logger.isDebugEnabled()) {
                logger.info("{}<<:mailNotifcations:[{}]", header, mailNotifcations);
            }
            return new MailGetDto(mailNotifcations);
        } catch (Exception e) {
            logger.error("{}Excep:getAllMailNotificationsByEntId:enterpriseId:{}:Error:{}", header, enterpriseId, ExceptionUtils.getRootCauseMessage(e));
            return new MailGetDto(new ArrayList<>());
        }
    }


}
