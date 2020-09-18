/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.qsmart2.utils.DateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Phani
 */
@Configuration
public class ObjectMapperConfig {
    
    @Bean
    public ObjectMapper initializeObjectMapper() {
        return new ObjectMapper();
    }
    
    
    @Bean
    public DateUtils dateUtils(){
        return new DateUtils();
    }
}
