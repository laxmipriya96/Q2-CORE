/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.qsmart2api.dtos.UserDetails;
import com.vm.qsmart2api.dtos.jwt.JwtUserDto;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 *
 * @author SOMANADH PHANI
 */
public class JwtRedisKeystore extends RedisTemplate<String, Object> {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private static String header = "[JWT_REDIS_TEMPLATE] ";

    @Autowired
    private ObjectMapper mapper;

//    @Value("${jwt.allow.session:4}")
//    private Integer allowSession;
    public JwtRedisKeystore() {
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        setKeySerializer(stringSerializer);
        setValueSerializer(valueSerializer);
        setHashKeySerializer(stringSerializer);
        setHashValueSerializer(valueSerializer);
    }

    public JwtRedisKeystore(RedisConnectionFactory connectionFactory) {
        this();
        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }

    public void saveJWTToken(int userId, String token, int m) {
        String setKey = (userId + "_" + token).toUpperCase();
        opsForValue().setIfAbsent(token, m);
        expire(setKey, 5, TimeUnit.MINUTES);
    }

    public void removeJWTTokenFromList(String key, UserDetails value, String key1) {
        try {
            logger.debug("{}Enter:removeJWTTokenFromList:key:[{}]:key1:[{}]", header, key, key1);
            opsForValue().getOperations().delete(key1);
            logger.info("{}Exit:removeJWTTokenFromList:Removed:key:[{}]:successfully:Id", header, key);
        } catch (Exception e) {
            logger.error("{}Excep:removeJWTTokenFromList:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public boolean isSessionActive(String header, String key, int allowSession) {
        try {
            logger.info("{}>>:isSessionActive:key:[{}]:allowSession:[{}]", header, key, allowSession);
            ScanOptions.ScanOptionsBuilder scanOptionsBuilder = new ScanOptions.ScanOptionsBuilder();
            scanOptionsBuilder.match(key + ":" + "*");
            JedisConnectionFactory connectionFactory = (JedisConnectionFactory) getConnectionFactory();
            RedisConnection connection = connectionFactory.getConnection();
            Cursor<byte[]> cursor = connection.scan(scanOptionsBuilder.build());
            int session = 0;
//            
//            if (allowSession == 1) {
//                while (cursor.hasNext()) {
//                    String ExistingKey = new String(cursor.next());
//                    removeJWTToken(ExistingKey);
//                    usersRepositary.loginEndTime(header, 0, ExistingKey, null);
//                    cursor.remove();
//
//                }
//            }
            while (cursor.hasNext()) {
                session++;
                String ExistingKey = new String(cursor.next());
                // System.out.println("****************************************ExistingKey:" + ExistingKey);
                logger.info("{}*************************************Session:Existing*************:{}:Keys:{}", header, session, ExistingKey);
                logger.debug("{}Session:Existing:{}:Keys:{}", header, session, ExistingKey);
            }
            cursor.close();
            connectionFactory.destroy();
            if (session < allowSession) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("{}Excep:isSessionActive:key:[{}]:Error:{}", header, key, ExceptionUtils.getRootCauseMessage(e));
            return true;
        }
    }

    public void removeJWTToken(String key) {
        try {
            logger.debug("{}>>:removeJWTToken:key:[{}]", header, key);
            expire(key, 0, TimeUnit.SECONDS);
            opsForValue().getOperations().delete(key);

            logger.info("{}Exit:removeJWTToken:Removed:key:[{}]:successfully:Id", header, key);
        } catch (Exception e) {
            logger.error("{}Excep:removeJWTToken:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public void setValue(final String key, final JwtUserDto value, TimeUnit unit, long timeout, boolean marshal) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("{}Enter:setValue:key:[{}], ExpirationTime:[{}], value:[{}]", header, key, timeout, value);
            }
            if (marshal) {
                setHashValueSerializer(new Jackson2JsonRedisSerializer<>(JwtUserDto.class));
                setValueSerializer(new Jackson2JsonRedisSerializer<>(JwtUserDto.class));
            } else {
                setHashValueSerializer(new StringRedisSerializer());
                setValueSerializer(new StringRedisSerializer());
            }

//            opsForSet().remove(key, value);
            //      opsForValue().getOperations().delete(key);
            //  opsForSet().add(key, value);
            opsForValue().set(key, value);

            //  opsForList().set(key, timeout + 1, value);
            expire(key, timeout, unit);

            // opsForValue().set(key, value);
            logger.debug("{}Exit:setValue:Saved:key:[{}], ExpirationTime:[{}], value:[{}]", header, key, timeout, value);
        } catch (Exception e) {
            logger.error("{}Excep:setValue:key:[{}]:Error:{}", header, key, ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public Object getValue(String header, final String key, Class clazz) {
        if (logger.isDebugEnabled()) {
            logger.info("{}key:[{}]", header, key);
        }
        setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        //    Object obj = opsForSet().pop(key);//get(key);
        Object obj = opsForValue().get(key);

        //  Object obj = opsForHash().get(key, key);
        return mapper.convertValue(obj, clazz);
    }

}
