/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.qsmart2.utils.CdrLog;
import com.vm.qsmart2core.service.CdrService;
import javax.jms.JMSException;
import javax.jms.Message;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;

/**
 *
 * @author Phani
 */
@Configuration
@EnableJms
public class CdrConsumer {
    
    private static final Logger logger = LogManager.getLogger(Hl7MsgConsumer.class);
    public static String header = "[CDR_CONSUMER] ";
    
    @Autowired
    ObjectMapper mapper;
    
    @Autowired
    CdrService cdrService;
    
    @JmsListener(destination = "${hl7.cdr.queue.name:Q2_HL7_CDR}")
    public void cdrReceiveMessage(final Message message) throws JMSException {
        try {
            ActiveMQTextMessage messageText = (ActiveMQTextMessage) message;
            logger.info("{}<<onMessage:Data:[{}]", header, messageText.getText());
            CdrLog cdrLog = mapper.readValue(messageText.getText(), CdrLog.class);
            cdrService.processCdrLog(("["+cdrLog.getTransId()+"_"+cdrLog.getStatus()+"] ").toUpperCase(), cdrLog);
            logger.info("{}<<cdrReceiveMessage:[{}]", header, cdrLog.toString());
        } catch (Exception e) {
            logger.error("{}Excep:cdrReceiveMessage:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
        }
    }
}
