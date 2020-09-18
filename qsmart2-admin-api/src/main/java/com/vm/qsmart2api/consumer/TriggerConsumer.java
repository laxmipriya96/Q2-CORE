/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.qsmart2.utils.TriggerTypeDTO;
import com.vm.qsmart2api.service.TriggerConsumerService;
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
 * @author Ashok
 */
@Configuration
@EnableJms
public class TriggerConsumer {

    private static final Logger logger = LogManager.getLogger(TriggerConsumer.class);
    public static String header = "[TRIGGER_CONSUMER] ";

    @Autowired
    ObjectMapper mapper;

    @Autowired
    TriggerConsumerService triggerConsumerService;

    @JmsListener(destination = "${qsmart.trigger.queue.name:QSMART_TRIGGER}")
    public void TriggerReceiveMessage(final Message message) throws JMSException {
        try {
            ActiveMQTextMessage messageText = (ActiveMQTextMessage) message;
            logger.info("{}<<onMessage:Data:[{}]", header, messageText.getText());
            TriggerTypeDTO triggerType = mapper.readValue(messageText.getText(), TriggerTypeDTO.class);
            triggerConsumerService.sendTriggerNotification(header, triggerType);
            logger.info("{}<<TriggerReceiveMessage:[{}]", header, triggerType.toString());
        } catch (Exception e) {
            logger.error("{}Excep:TriggerReceiveMessage:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
        }
    }

}
