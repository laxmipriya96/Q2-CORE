/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.qsmart2.utils.Hl7MsgTrigger;
import com.vm.qsmart2core.service.Hl7TriggerService;
import javax.jms.JMSException;
import javax.jms.Message;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author Phani
 */
@Configuration
@EnableJms
public class Hl7ProcessConsumer {

    private static final Logger logger = LogManager.getLogger(Hl7MsgConsumer.class);

    public static String header = "[HL7_PROCESS_CONSUMER] ";

    @Autowired
    ObjectMapper mapper;

    @Autowired
    Hl7TriggerService hl7TriggerSer;

//    @Value("${hl7.producer.queue.name:Q2_HL7_PROCESS}")
//    private String processQName;
    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${app.debug.required:false}")
    private boolean isLogEnabled;

    @JmsListener(destination = "${hl7.process.queue.name:Q2_HL7_PROCESS}")
    public void hl7ReceiveMessage(final Message message) throws JMSException {
        try {
            ActiveMQTextMessage messageText = (ActiveMQTextMessage) message;
            if (isLogEnabled) {
                logger.info("{}>>Data:[{}]", header, messageText.getText());
            }
            Hl7MsgTrigger hl7Trigger = mapper.readValue(messageText.getText(), Hl7MsgTrigger.class);
            boolean isSuccess = hl7TriggerSer.hl7TriggerProcess("[HL7_TRIGGER_SERVICE] ", hl7Trigger);
            if (isLogEnabled) {
                logger.info("{}<<hltTriggerProcess:[{}]", header, isSuccess);
            }
        } catch (Exception e) {
            logger.error("{}Excep:hl7ReceiveMessage:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
        }
    }

}
