/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.service;

import com.vm.qsmart2.model.TblGlobalSetting;
import com.vm.qsmart2.model.TblMailQueue;
import com.vm.qsmart2.model.TblUser;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.repository.GlobalSettingsRepositary;
import com.vm.qsmart2api.repository.MailQueueRepository;
import com.vm.qsmart2api.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.text.StrSubstitutor;
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
public class MailQueueService {

    public static final Logger logger = LogManager.getLogger(MailQueueService.class);

    @Value("${mail.template}")
    private String mailTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MailQueueRepository mailQueueRepository;

    @Autowired
    GlobalSettingsRepositary gRepositary;

    @Value("${app.debug.required:true}")
    private boolean isLogEnabled;

    @Autowired
    DateUtils dateUtils;

    public void sendMail(String header, Long id) {
        if (isLogEnabled) {
            logger.info("{}>>:{}:sendMail:{}:id:{}", header, id);
        }
        try {
            TblUser user = userRepository.getOne(id);
            Map<String, String> map = new HashMap();
            map.put("firstName", user.getFirstName());
            map.put("lastName", user.getLastName());
            map.put("userName", user.getUserName());
            map.put("password", user.getHashPassword());
            String message = StrSubstitutor.replace(mailTemplate, map, "{", "}");
            System.out.println(">>>>>>message::" + message);
            saveMailQueue(header, user, message);
            if (isLogEnabled) {
                logger.info("{}<<:{}:sendMail:{}:id:{}:Message:{}", header, id, message);
            }
        } catch (Exception e) {
            logger.error("{}Excep:sendMail:id:{}:Error:{}", header, id, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    private void saveMailQueue(String header, TblUser user, String message) {
        if (isLogEnabled) {
            logger.info("{}>>:{}:saveMailQueue:{}:TblUser:{}:message:{}", header, user, message);
        }
        try {
            TblMailQueue mail = new TblMailQueue();
            TblGlobalSetting globalSettings = gRepositary.getGlobalSettingsByType("SMTP", user.getEnterprise().getEnterpriseId());
            mail.setMailAccount(globalSettings.getId());
            mail.setEnterpriseId(user.getEnterprise().getEnterpriseId());
            mail.setToMail(user.getEmailId());
            mail.setMailSubject("user creation");
            mail.setMailBody(message);
            mail.setStatus(0);
            mail.setCreatedOn(dateUtils.getdate());
            mail.setSubmitDate(dateUtils.getdate());
            mailQueueRepository.save(mail);
            if (isLogEnabled) {
                logger.info("{}<<:{}:saveMailQueue:TblUser:{}:message:{}:TblMailQueue:{}", header, user, message, mail);
            }
        } catch (Exception e) {
            logger.error("{}Excep:saveMailQueue:TblUser:{}:message:{}:Error:{}", header, user, message, ExceptionUtils.getRootCauseMessage(e));
        }
    }

}
