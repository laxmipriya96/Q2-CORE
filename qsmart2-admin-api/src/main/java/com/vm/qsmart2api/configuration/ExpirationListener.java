/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.configuration;

import com.vm.qsmart2api.repository.UserRepository;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

/**
 *
 * @author SOMANADH PHANI
 */
@Service
public class ExpirationListener implements MessageListener {

    private static final Logger logger = LogManager.getLogger(ExpirationListener.class);
    private static String header = "[EXPIRATION_LISTENER] ";
    private UserRepository repositary;
    private JwtRedisKeystore redisTemplate;
    public ExpirationListener() {
        //logger.info("<=========================>Listener Started<===============================>");
    }

    public ExpirationListener(JwtRedisKeystore redisTemplate, UserRepository repositary) {
        logger.info("<=========================>ExpirationListener Started<===============================>");
        this.redisTemplate = redisTemplate;
        this.repositary = repositary;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String key = null;
        try {
            key = new String(message.getBody());
            logger.info("{}Enter:ExpirationListener:onMessage:key:[{}]", header, key);
        } catch (Exception e) {
            logger.info("{}Excep:ExpirationListener:onMessage:Error:[{}]", header, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            key = null;
        }
    }

}
