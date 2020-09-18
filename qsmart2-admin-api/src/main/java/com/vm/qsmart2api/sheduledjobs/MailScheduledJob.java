/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.sheduledjobs;

import com.vm.qsmart2api.service.MailAndSmsQueueService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ashok
 */
@Service
@ConditionalOnProperty(
    value="mail.enable.required", 
    havingValue = "true", 
    matchIfMissing = true)
public class MailScheduledJob {

    private static final Logger logger = LogManager.getLogger(MailScheduledJob.class);

    @Autowired
    MailAndSmsQueueService mailAndSmsQueueService;

    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    public void sendMail() {
       // logger.info("{}>>MailScheduledJob:Request:[{}]");
        mailAndSmsQueueService.sendPendingMails();
    }

}
