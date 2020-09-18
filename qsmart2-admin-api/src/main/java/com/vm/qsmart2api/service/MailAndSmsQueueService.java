/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.qsmart2.model.TblGlobalSetting;
import com.vm.qsmart2.model.TblMailQueue;
import com.vm.qsmart2.model.TblNotificationMail;
import com.vm.qsmart2.model.TblSmsQueue;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2.utils.TriggerParams;
import com.vm.qsmart2api.dtos.global.MailServerSettings;
import com.vm.qsmart2api.dtos.global.QueryParam;
import com.vm.qsmart2api.dtos.global.SmsSettings;
import com.vm.qsmart2api.repository.GlobalSettingsRepositary;
import com.vm.qsmart2api.repository.LocationRepository;
import com.vm.qsmart2api.repository.MailNotificationRepository;
import com.vm.qsmart2api.repository.MailQueueRepository;
import com.vm.qsmart2api.repository.NotificationSmsRepository;
import com.vm.qsmart2api.repository.ServiceBookedRepository;
import com.vm.qsmart2api.repository.SmsQueueRepository;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Ashok
 */
@Service
public class MailAndSmsQueueService {

    private static final Logger logger = LogManager.getLogger(MailAndSmsQueueService.class);

    @Autowired
    MailQueueRepository mailQueueRepository;

    @Autowired
    GlobalSettingsRepositary gRepositary;

    @Autowired
    ObjectMapper objMappaer;

    @Autowired
    SmsQueueRepository smsQueueRepository;
    
    @Autowired
    ServiceBookedRepository serviceBookedRepository;

    @Autowired
    NotificationSmsRepository notificationSmsRepository;

    @Autowired
    MailNotificationRepository mailNotificationRepository;

    @Autowired
    LocationRepository locationRepository;


    @Autowired
    DateUtils dateUtils;

    @Autowired
    RestTemplate template;

    private static final String emailHeader = "[SENT_EMAIL_JOB] ";
    private static final String smsHeader = "[SENT_SMS_JOB] ";

    @Value("${app.debug.required:true}")
    private boolean isLogEnabled;

    public void sendPendingMails() {
        MailServerSettings mailServerSettings = null;
        TblGlobalSetting globalSettings = null;
        if (isLogEnabled) {
            logger.info("{}>>STATRING>>", emailHeader);
        }
        try {
            List<TblMailQueue> mailQueue = mailQueueRepository.getAllPendingMails(0);
            for (TblMailQueue mail : mailQueue) {
                globalSettings = gRepositary.getGlobalSettingsByType("SMTP", mail.getEnterpriseId());
                if (globalSettings != null) {
                    String jsonString = globalSettings.getSettingJson();
                    mailServerSettings = objMappaer.readValue(jsonString, MailServerSettings.class);
                }
                sendMail(mail, mailServerSettings);
            }
            if (isLogEnabled) {
                logger.info("{}<<Exit:processMailsSize:[{}]", emailHeader, mailQueue.size());
            }
        } catch (Exception e) {
            logger.error("{}Excep:sendPendingMails:Error:{}", emailHeader, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            mailServerSettings = null;
            globalSettings = null;
        }
    }

    private void sendMail(TblMailQueue mail, MailServerSettings mailServerSettings) {
        if (isLogEnabled) {
            logger.info("{}>>TblMailQueue:{}:MailServerSettings:{}", emailHeader, mail, mailServerSettings);
        }
        Properties prop = new Properties();
        prop.put("mail.smtp.host", mailServerSettings.getServerIp());
        prop.put("mail.smtp.port", mailServerSettings.getPort());
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailServerSettings.getEmailAccount(), mailServerSettings.getPassword());
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailServerSettings.getEmailAccount()));
            message.addRecipient(
                    MimeMessage.RecipientType.TO,
                    new InternetAddress(mail.getToMail())
            );
            message.setSubject(mail.getMailSubject());
            message.setText(mail.getMailBody());
            Transport.send(message);
            updateMail(mail, 123, "success");
            if (isLogEnabled) {
                logger.info("{}<<sendMail:Response:[{}]:message:{}:mail:{}:mailServerSettings:{}", message, mail, mailServerSettings);
            }
        } catch (MessagingException e) {
            logger.error("{}Excep:sendMail:mail:{}:mailServerSettings:{}:Error:{}", mail, mailServerSettings, ExceptionUtils.getRootCauseMessage(e));
            updateMail(mail, 124, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public void sendPendingSms() {
        if (isLogEnabled) {
            logger.info("{}>>STARTING>>", smsHeader);
        }
        TblGlobalSetting globalSettings = null;
        SmsSettings smsSettings = null;
        try {
            List<TblSmsQueue> smsQueue = smsQueueRepository.getAllPendingSms(0);
            for (TblSmsQueue sms : smsQueue) {
                globalSettings = gRepositary.getGlobalSettingsByType("SMS", sms.getEnterpriseId());
                if (globalSettings != null) {
                    String jsonString = globalSettings.getSettingJson();
                    smsSettings = objMappaer.readValue(jsonString, SmsSettings.class);
                }
                sendSms(sms, smsSettings);
            }
            if (isLogEnabled) {
                logger.info("{}<<Exit:processSmsSize:[{}]", smsHeader, smsQueue.size());
            }
        } catch (Exception e) {
            logger.error("{}Excep:sendPendingSms:Error:{}", smsHeader, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            globalSettings = null;
            smsSettings = null;
        }
    }

    private void sendSms(TblSmsQueue sms, SmsSettings smsSettings) {
        if (isLogEnabled) {
            logger.info("{}>>sendSms:TblSmsQueue:[{}]:SmsSettings:[{}]", smsHeader, sms, smsSettings);
        }
        try {
            StringBuilder url = new StringBuilder();
            StringBuilder param = new StringBuilder();
            url.append(smsSettings.getTypeOfApi()).append("://").append(smsSettings.getHost()).append("/").append(smsSettings.getPath());
            param.append("?");
            for (QueryParam queryParam : smsSettings.getQueryParameters()) {
                param.append(queryParam.getParamName()).append("=");
                String value = queryParam.getParamValue();
                switch (value) {
                    case "Message":
                        param.append(sms.getSmsText()).append("&");
                        break;
                    case "Mobile Number":
                        param.append(sms.getMobileNo()).append("&");
                        break;
                    case "Data Coding":
                        param.append((sms.getSmsText().getBytes(Charset.forName("UTF-8")).length > sms.getSmsText().length() ? 8 : 1)).append("&");
                        break;
                    default:
                        param.append(value).append("&");
                }
            }
            url.append(param.deleteCharAt(param.length() - 1));
            if (isLogEnabled) {
                logger.info("{}>>sendSms:url:[{}]", smsHeader, url);
            }
            ResponseEntity<String> result = template.getForEntity(url.toString(), String.class);
            if (result.getStatusCodeValue() == 200) {
                updateSms(sms, result.getStatusCodeValue(), "succuss");
                smsQueueRepository.save(sms);
            } else {
                updateSms(sms, result.getStatusCodeValue(), "failed");
                smsQueueRepository.save(sms);
            }
        } catch (Exception e) {
            logger.error("{}Excep:sendSms:sms:{}:smsSettings:{}:Error:{}", smsHeader, sms, smsSettings, ExceptionUtils.getRootCauseMessage(e));
            updateSms(sms, 123, ExceptionUtils.getRootCauseMessage(e));
            smsQueueRepository.save(sms);
        }
    }

    private void updateSms(TblSmsQueue sms, int statusCodeValue, String msg) {
        if (isLogEnabled) {
            logger.info("{}>>updateSms:TblSmsQueue:[{}]", smsHeader, sms);
        }
        try {
            sms.setStatus(1);
            sms.setSubmitDate(dateUtils.getdate());
            sms.setErrCode(statusCodeValue);
            sms.setErrText(msg);
            smsQueueRepository.save(sms);
        } catch (Exception e) {
            logger.error("{}Excep:updateSms:TblSmsQueue:{}:Error:{}", smsHeader, sms, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    private void updateMail(TblMailQueue mail, int statusCodeValue, String msg) {
        if (isLogEnabled) {
            logger.info("{}>>updateMail:TblMailQueue:[{}]", emailHeader, mail);
        }
        try {
            mail.setStatus(1);
            mail.setSubmitDate(dateUtils.getdate());
            mail.setErrCode(statusCodeValue);
            mail.setErrText(msg);
            mailQueueRepository.save(mail);
        } catch (Exception e) {
            logger.error("{}Excep:updateMail:TblMailQueue:{}:Error:{}", smsHeader, mail, ExceptionUtils.getRootCauseMessage(e));
        }
    }
    
    public void sendReminderSmsNotification() {
        if (isLogEnabled) {
            logger.info("{}>>STARTING>>", smsHeader);
        }
        try {
            Date fromDate = dateUtils.dayStartDateTime();
            Date toDate = dateUtils.dayEndDateTime();
            String smsTemplate;
            String smsMessage = null;
            String emailMessage = null;
            List<TriggerParams> scheduledAppointments = serviceBookedRepository.getScheduledAppointments("SCHEDULED", fromDate, toDate);
            for (TriggerParams params : scheduledAppointments) {
                smsTemplate = notificationSmsRepository.getTemplateText(params.getEnterpriseId(), "RMSG");
                if (smsTemplate != null && !smsTemplate.isEmpty()) {
                    smsMessage = params.getTriggerTemplate(params, smsTemplate);
                    saveAndSendSms(smsMessage, params);
                }
                TblNotificationMail notifictnMail = mailNotificationRepository.getTemplateText(params.getEnterpriseId(), "RMSG");
                if (notifictnMail != null) {
                    emailMessage = params.getTriggerTemplate(params, notifictnMail.getMailBody());
                    saveAndSendEmail(notifictnMail, emailMessage, params);
                }
            }
            if (isLogEnabled) {
                logger.info("{}<<Exit:processSmsSize:[{}]", smsHeader, scheduledAppointments.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}Excep:sendPendingSms:Error:{}", smsHeader, ExceptionUtils.getRootCauseMessage(e));
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
            e.printStackTrace();
            logger.error("{}Excep:saveAndSendSms:smsMessage:{}:TriggerParams:{}:Error:{}", smsMessage, triggerParams, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    private void saveAndSendEmail(TblNotificationMail notifictnMail, String emailMessage, TriggerParams triggerParams) {
        if (isLogEnabled) {
            logger.info("{}<<saveAndSendEmail:smsMessage:{}:TriggerParams:{}", emailMessage, triggerParams);
        }
        try {
            Long locationId = locationRepository.getLocationIdByLocationName(triggerParams.getLOCATION().toUpperCase(), triggerParams.getEnterpriseId());
            TblGlobalSetting globalSettings = gRepositary.getGlobalSettingsByType("SMTP", triggerParams.getEnterpriseId());
            TblMailQueue mail = new TblMailQueue();
            mail.setMailBody(emailMessage);
            mail.setMailSubject(notifictnMail.getMailSubject());
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
