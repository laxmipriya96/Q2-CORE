/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.configuration;

import com.vm.qsmart2api.contoller.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author Phani
 */
@Component
public class RuntimeBeanBuilder {

    @Autowired
    private ApplicationContext applicationContext;

    public AuthController load(String beanName, AuthController myObject) {
        ConfigurableApplicationContext configContext = (ConfigurableApplicationContext) applicationContext;
        SingletonBeanRegistry beanRegistry = configContext.getBeanFactory();
        if (beanRegistry.containsSingleton(beanName)) {
            System.out.println("-----------> if");
            return (AuthController) beanRegistry.getSingleton(beanName);
        } else {
            beanRegistry.registerSingleton(beanName, myObject);
            System.out.println("-----------> else");
            String[] beans = configContext.getBeanDefinitionNames();
            for (String s : beans) {
                 System.out.println("Bean Name : " + s);
            }
            return (AuthController) beanRegistry.getSingleton(beanName);
        }
    }
}
