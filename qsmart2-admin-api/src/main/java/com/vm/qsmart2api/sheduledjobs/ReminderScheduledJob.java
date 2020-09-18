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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Tejasri
 */
@Component
public class ReminderScheduledJob {

    private static final Logger logger = LogManager.getLogger(ReminderScheduledJob.class);

    @Autowired
    MailAndSmsQueueService mailAndSmsQueueService;

    @Scheduled(cron = "0 5 0 * * ?")
    public void sendReminderSms() {
        mailAndSmsQueueService.sendReminderSmsNotification();
    }
}
