/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.configuration;

import com.vm.qsmart2api.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 *
 * @author SOMANADH PHANI
 */
@Configuration
public class RedistConfiguration {

    @Value("${redis.host}")
    private String REDIS_HOST;

    @Value("${redis.port}")
    private int REDIS_PORT;

    @Value("${redis.auth}")
    private String REDIS_AUTH;
    
    @Autowired
    UserRepository repositary;
    
    private static final Logger logger = LogManager.getLogger(RedistConfiguration.class);

    @Bean
    public JedisConnectionFactory connectionFactory() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(REDIS_HOST);
        connectionFactory.setPort(REDIS_PORT);
        //connectionFactory.setDatabase(1);
        if (REDIS_AUTH != null && !REDIS_AUTH.isEmpty()) {
            connectionFactory.setPassword(REDIS_AUTH);
        }
        return connectionFactory;
    }

    @Bean
    public JwtRedisKeystore redisTemplate() {
        final JwtRedisKeystore template = new JwtRedisKeystore(connectionFactory());
        return template;
    }
    
    
    @Bean
    public RedisMessageListenerContainer keyExpirationListenerContainer() {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.addMessageListener(new ExpirationListener(redisTemplate(),repositary), new PatternTopic("__keyevent@*__:expired"));
        listenerContainer.setErrorHandler(e -> logger.error("There was an error in redis key expiration listener container", e));
        logger.info("<=========================>Listender Subscribed<===============================>");
        return listenerContainer;
    }
}
