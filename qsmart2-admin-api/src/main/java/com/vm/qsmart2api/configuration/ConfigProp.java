/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.configuration;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Phani
 */
@ConfigurationProperties(prefix = "config")
@Configuration
public class ConfigProp {
    
    
//    private Map<Long, Long> expiryMap;
//
//    public Map<Long, Long> getExpiryMap() {
//        return expiryMap;
//    }
//
//    public void setExpiryMap(Map<Long, Long> expiryMap) {
//        this.expiryMap = expiryMap;
//    }
    
      private long expiration = 10;

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
      
}
