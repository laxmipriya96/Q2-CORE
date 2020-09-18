/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblGlobalSetting;
import com.vm.qsmart2.model.TblMailQueue;
import com.vm.qsmart2.model.TblNotificationMail;
import com.vm.qsmart2.model.TblSmsQueue;
import com.vm.qsmart2.utils.TriggerParams;
import com.vm.qsmart2.utils.TriggerType;
import com.vm.qsmart2.utils.TriggerTypeDTO;
import com.vm.qsmart2api.repository.GlobalSettingsRepositary;
import com.vm.qsmart2api.repository.LocationRepository;
import com.vm.qsmart2api.repository.MailNotificationRepository;
import com.vm.qsmart2api.repository.MailQueueRepository;
import com.vm.qsmart2api.repository.NotificationSmsRepository;
import com.vm.qsmart2api.repository.ServiceBookedRepository;
import com.vm.qsmart2api.repository.SmsQueueRepository;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ashok
 */
@Service
public class TriggerConsumerService {

    @Autowired
    ServiceBookedRepository serviceBookedRepository;

    @Autowired
    NotificationSmsRepository notificationSmsRepository;

    @Autowired
    MailNotificationRepository mailNotificationRepository;

    @Autowired
    SmsQueueRepository smsQueueRepository;

    @Autowired
    MailQueueRepository mailQueueRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    GlobalSettingsRepositary gRepositary;

    @Value("${app.debug.required:true}")
    private boolean isLogEnabled;

    private static final Logger logger = LogManager.getLogger(TriggerConsumerService.class);

    public void sendTriggerNotification(String header, TriggerTypeDTO triggerType) {
        if (isLogEnabled) {
            logger.info("{}<<sendTriggerNotification:TriggerType:[{}]", header, triggerType);
        }
        try {
            TriggerParams triggerParams = new TriggerParams();
            String smsTemplate;
            String smsMessage;
            String emailMessage;
            if (triggerType.getTriggerType().equalsIgnoreCase(TriggerType.AMSG.getValue())) {
                triggerParams = serviceBookedRepository.getAppointmentDetails(triggerType.getTranId(), triggerType.getServiceId());
                smsTemplate = notificationSmsRepository.getTemplateText(triggerParams.getEnterpriseId(), "AMSG");
                if (smsTemplate != null && !smsTemplate.isEmpty()) {
                    smsMessage = triggerParams.getTriggerTemplate(triggerParams, smsTemplate);
                    saveAndSendSms(smsMessage, triggerParams);
                }
                TblNotificationMail notifiMail = mailNotificationRepository.getTemplateText(triggerParams.getEnterpriseId(), "AMSG");
                if (notifiMail != null) {
                    emailMessage = triggerParams.getTriggerTemplate(triggerParams, notifiMail.getMailBody());
                    saveAndSendEmail(notifiMail.getMailSubject(), emailMessage, triggerParams);
                }
            } else {
                triggerParams = serviceBookedRepository.getCheckInAppointmentDetails(triggerType.getTranId(), triggerType.getServiceId());
                smsTemplate = notificationSmsRepository.getTemplateText(triggerParams.getEnterpriseId(), "CMSG");
                if (!smsTemplate.isEmpty()) {
                    smsMessage = triggerParams.getTriggerTemplate(triggerParams, smsTemplate);
                    saveAndSendSms(smsMessage, triggerParams);
                }
                TblNotificationMail notifiMail = mailNotificationRepository.getTemplateText(triggerParams.getEnterpriseId(), "CMSG");
                if (notifiMail != null) {
                    emailMessage = triggerParams.getTriggerTemplate(triggerParams, notifiMail.getMailBody());
                    saveAndSendEmail(notifiMail.getMailSubject(), emailMessage, triggerParams);
                }
            }
        } catch (Exception e) {
            logger.error("{}Excep:sendTriggerNotification:TriggerType:{}:Error:{}", header, triggerType, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    private void saveAndSendSms(String smsMessage, TriggerParams triggerParams) {
        if (isLogEnabled) {
            logger.info("{}<<saveAndSendSms:smsMessage:{}:TriggerParams:{}", smsMessage, triggerParams);
        }
        try {
            Long locationId = locationRepository.getLocationIdByLocationName(triggerParams.getLOCATION().toUpperCase(), triggerParams.getEnterpriseId());
            TblGlobalSetting globalSettings = gRepositary.getGlobalSettingsByType("SMS", triggerParams.getEnterpriseId());
            TblSmsQueue sms = new TblSmsQueue();
            sms.setSmsText(smsMessage);
            sms.setMobileNo(triggerParams.getCONTACTNO());
            sms.setEnterpriseId(triggerParams.getEnterpriseId());
            sms.setLocationId(locationId);
            sms.setSmsAccount(globalSettings.getId() != null ? globalSettings.getId() : 0);
            sms.setStatus(0);
            smsQueueRepository.save(sms);
        } catch (Exception e) {
            logger.error("{}Excep:saveAndSendSms:smsMessage:{}:TriggerParams:{}:Error:{}", smsMessage, triggerParams, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    private void saveAndSendEmail(String mailSubject, String emailMessage, TriggerParams triggerParams) {
        if (isLogEnabled) {
            logger.info("{}<<saveAndSendEmail:smsMessage:{}:TriggerParams:{}", emailMessage, triggerParams);
        }
        try {
            Long locationId = locationRepository.getLocationIdByLocationName(triggerParams.getLOCATION().toUpperCase(), triggerParams.getEnterpriseId());
            TblGlobalSetting globalSettings = gRepositary.getGlobalSettingsByType("SMTP", triggerParams.getEnterpriseId());
            TblMailQueue mail = new TblMailQueue();
            mail.setMailSubject(mailSubject);
            mail.setMailBody(emailMessage);
            mail.setToMail(triggerParams.getMAILADDRESS());
            mail.setEnterpriseId(triggerParams.getEnterpriseId());
            mail.setLocationId(locationId);
            mail.setMailAccount(globalSettings.getId() != null ? globalSettings.getId() : 0);
            mail.setStatus(0);
            mailQueueRepository.save(mail);
        } catch (Exception e) {
            logger.error("{}Excep:saveAndSendEmail:emailMessage:{}:TriggerParams:{}:Error:{}", emailMessage, triggerParams, ExceptionUtils.getRootCauseMessage(e));
        }
    }

}
